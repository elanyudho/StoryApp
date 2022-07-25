package com.elanyudho.core.extension

import com.elanyudho.core.vo.Either

inline fun <L, R> Either<L, R>.onError(error : (L) -> Unit) : Either<L, R> {
    if (this is Either.Error) {
        error(this.failure)
    }
    return this
}

inline fun <L, R> Either<L, R>.onSuccess(success : (R) -> Unit) : Either<L, R> {
    if (this is Either.Success) {
        success(this.body)
    }
    return this
}