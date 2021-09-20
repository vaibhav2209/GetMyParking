package com.example.getmyparking.data

import com.example.getmyparking.utils.Resource
import kotlinx.coroutines.flow.*

inline fun <ResultType, RequestType> networkBoundResource(
    crossinline query: () -> Flow<ResultType>,
    crossinline fetch: suspend () -> RequestType,
    crossinline saveFetchResult: suspend (RequestType) -> Resource<RequestType>,
    crossinline shouldFetch: (ResultType) -> Boolean
) = flow {
    val data = query().first()

    val flow = if (shouldFetch(data)) {
        emit(Resource.Loading(data))

        try {
            saveFetchResult(fetch())
            query().map { Resource.Success(it) }
        } catch (throwable: Throwable) {
            query().map { throwable.message?.let { it1 -> Resource.Error(it1, it) } }
        }
    } else {
        query().map { Resource.Success(it) }
    }

    emitAll(flow)
}

/*inline fun <T> networkBoundResource2(
    crossinline query: () -> Flow<T>,
    crossinline fetch: suspend () -> T,
    crossinline saveFetchResult: suspend (T) -> Unit,
    crossinline shouldFetch: () -> Boolean
) = flow<Resource<T>> {
    val flow = if (shouldFetch()) {
        emit(Resource.Loading())
        saveFetchResult(fetch())
        fetch()
    } else {
       query().map { Resource.Success(it) }
    }
    emitAll(flow)
}*/