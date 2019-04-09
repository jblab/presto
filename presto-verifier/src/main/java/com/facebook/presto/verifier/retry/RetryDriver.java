/*
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.facebook.presto.verifier.retry;

import com.facebook.presto.verifier.framework.QueryException;
import com.facebook.presto.verifier.framework.VerificationContext;
import io.airlift.log.Logger;
import io.airlift.units.Duration;

import java.util.concurrent.ThreadLocalRandom;
import java.util.function.Predicate;

import static java.lang.Math.max;
import static java.lang.Math.min;
import static java.lang.Math.pow;
import static java.lang.Thread.currentThread;
import static java.util.Objects.requireNonNull;
import static java.util.concurrent.TimeUnit.MILLISECONDS;

public class RetryDriver
{
    private static final Logger log = Logger.get(RetryDriver.class);

    private final int maxAttempts;
    private final Duration minBackoffDelay;
    private final Duration maxBackoffDelay;
    private final double scaleFactor;
    private final Predicate<QueryException> retryPredicate;

    public RetryDriver(
            RetryConfig config,
            Predicate<QueryException> retryPredicate)
    {
        this.maxAttempts = config.getMaxAttempts();
        this.minBackoffDelay = requireNonNull(config.getMinBackoffDelay(), "minBackoffDelay is null");
        this.maxBackoffDelay = requireNonNull(config.getMaxBackoffDelay(), "maxBackoffDelay is null");
        this.scaleFactor = config.getScaleFactor();
        this.retryPredicate = requireNonNull(retryPredicate, "retryPredicate is null");
    }

    public <V> V run(String callableName, VerificationContext context, RetryOperation<V> operation)
    {
        int attempt = 1;
        while (true) {
            try {
                return operation.run();
            }
            catch (QueryException qe) {
                context.recordFailure(qe);
                if (attempt >= maxAttempts || !retryPredicate.test(qe)) {
                    throw qe;
                }

                attempt++;
                int delayMillis = (int) min(minBackoffDelay.toMillis() * pow(scaleFactor, attempt - 1), maxBackoffDelay.toMillis());
                int jitterMillis = ThreadLocalRandom.current().nextInt(max(1, (int) (delayMillis * 0.1)));
                log.debug(
                        "Failed on executing %s(%s, %s) with attempt %d. Retry after %sms. Cause: %s",
                        callableName,
                        qe.getQueryOrigin().getGroup(),
                        qe.getQueryOrigin().getStage(),
                        attempt - 1,
                        delayMillis,
                        qe.getErrorCode());

                try {
                    MILLISECONDS.sleep(delayMillis + jitterMillis);
                }
                catch (InterruptedException ie) {
                    currentThread().interrupt();
                    throw new RuntimeException(ie);
                }
            }
        }
    }

    public interface RetryOperation<V>
    {
        V run()
                throws QueryException;
    }
}
