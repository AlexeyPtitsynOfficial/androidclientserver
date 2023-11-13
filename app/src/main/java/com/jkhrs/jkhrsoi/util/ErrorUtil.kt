package com.jkhrs.jkhrsoi.util

import com.jkhrs.jkhrsoi.data.source.remote.datasources.ServiceGenerator
import okhttp3.ResponseBody
import retrofit2.Converter
import retrofit2.Response
import java.io.IOException


class ErrorUtil {

    companion object {
        fun parseError(response: Response<*>): APIError? {
            val converter: Converter<ResponseBody, APIError> = ServiceGenerator.retrofit
                .responseBodyConverter(APIError::class.java, arrayOfNulls<Annotation>(0))
            val error: APIError
            error = try {
                converter.convert(response.errorBody())!!
            } catch (e: IOException) {
                return APIError()
            }
            return error
        }
    }
}