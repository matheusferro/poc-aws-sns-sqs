package me.pocSnsSqs.exceptionHandler

import com.google.rpc.BadRequest
import com.google.rpc.Code
import io.grpc.BindableService
import io.grpc.Status
import io.grpc.StatusRuntimeException
import io.grpc.protobuf.StatusProto
import io.grpc.stub.StreamObserver
import io.micronaut.aop.InterceptorBean
import io.micronaut.aop.MethodInterceptor
import io.micronaut.aop.MethodInvocationContext
import jakarta.inject.Singleton
import org.slf4j.LoggerFactory
import javax.validation.ConstraintViolationException

@Singleton
@InterceptorBean(ErrorHandler::class)
class ExceptionHandlerInterceptor() : MethodInterceptor<BindableService, Any?> {

    private val logger = LoggerFactory.getLogger(this::class.java)

    override fun intercept(context: MethodInvocationContext<BindableService, Any?>): Any? {
        return try {
            context.proceed()
        } catch (e: Exception) {

            val errorGrpc = when (e) {
                is ConstraintViolationException -> handleConstraintViolationException(e)
                else -> Status.UNKNOWN.withDescription("Unexpected error").asRuntimeException()
            }
            logger.error("Error occurred: ${e.message}")
            val responseObserver = context.parameterValues[1] as StreamObserver<*>
            responseObserver.onError(errorGrpc)
            null
        }
    }

    private fun handleConstraintViolationException(e: ConstraintViolationException): StatusRuntimeException {
        val badReq = BadRequest.newBuilder().addAllFieldViolations(
            e.constraintViolations.map {
                BadRequest.FieldViolation.newBuilder()
                    .setField(it.propertyPath.last().name)
                    .setDescription(it.message)
                    .build()
            }
        ).build()

        val statusProto = com.google.rpc.Status.newBuilder()
            .setCode(Code.INVALID_ARGUMENT_VALUE)
            .setMessage("Request with invalid parameters")
            .addDetails(com.google.protobuf.Any.pack(badReq))
            .build()
        return StatusProto.toStatusRuntimeException(statusProto)
    }
}