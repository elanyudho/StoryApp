package com.elanyudho.core.abstraction

import com.elanyudho.core.exception.Failure
import com.elanyudho.core.vo.Either

abstract class UseCase<out Type, in Params> {

    abstract suspend fun run(params: Params): Either<Failure, Type>

    object None

}