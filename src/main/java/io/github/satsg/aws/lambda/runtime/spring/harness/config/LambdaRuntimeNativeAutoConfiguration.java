package io.github.satsg.aws.lambda.runtime.spring.harness.config;

import io.github.satsg.aws.lambda.runtime.spring.harness.event.AWSLambdaCustomResponse;
import io.github.satsg.aws.lambda.runtime.spring.harness.event.AWSLambdaErrorResponse;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Configuration;
import org.springframework.nativex.hint.AccessBits;
import org.springframework.nativex.hint.NativeHint;
import org.springframework.nativex.hint.TypeHint;

@ConditionalOnProperty(value = "satsg.enable.aws.lambda.runtime.configuration")
@NativeHint(
    types =
        @TypeHint(
            types = {AWSLambdaCustomResponse.class, AWSLambdaErrorResponse.class},
            access =
                AccessBits.DECLARED_METHODS
                    | AccessBits.DECLARED_FIELDS
                    | AccessBits.DECLARED_CONSTRUCTORS),
    options = {"--enable-http"})
@Configuration
public class LambdaRuntimeNativeAutoConfiguration {}
