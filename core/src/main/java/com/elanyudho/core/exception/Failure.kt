package com.elanyudho.core.exception

import com.elanyudho.core.vo.RequestResults

data class  Failure(val requestResults: RequestResults, val throwable: Throwable, val code:String="")