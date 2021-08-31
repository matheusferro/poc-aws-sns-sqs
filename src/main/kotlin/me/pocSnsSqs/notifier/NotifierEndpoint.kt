package me.pocSnsSqs.notifier

import io.grpc.stub.StreamObserver
import jakarta.inject.Singleton
import me.pocSnsSqs.NotifierApiServiceGrpc
import me.pocSnsSqs.SendCustomerDataRequest
import me.pocSnsSqs.SendCustomerDataResponse
import me.pocSnsSqs.customer.CustomerModel
import me.pocSnsSqs.customer.CustomerService
import me.pocSnsSqs.customer.NOTIFICATION
import me.pocSnsSqs.exceptionHandler.ErrorHandler
import java.security.InvalidParameterException

@Singleton
@ErrorHandler
class NotifierEndpoint(
    private val customerService: CustomerService
): NotifierApiServiceGrpc.NotifierApiServiceImplBase() {

    override fun sendCustomerData(
        request: SendCustomerDataRequest?,
        responseObserver: StreamObserver<SendCustomerDataResponse>?
    ) {
        request ?: throw InvalidParameterException("Error on verify request")
        responseObserver ?: throw InvalidParameterException("Error on verify request")
        customerService.sendCustomerData(request.toModel())
        responseObserver.onNext(
            SendCustomerDataResponse.newBuilder().apply {
                this.message = "Subscription successful."
            }.build()
        )
        responseObserver.onCompleted()
    }

    private fun SendCustomerDataRequest.toModel(): CustomerModel = CustomerModel(this.name, this.email, this.phone, NOTIFICATION.valueOf(this.notifyOption.toString()))
}