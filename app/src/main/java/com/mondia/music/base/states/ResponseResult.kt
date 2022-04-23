package com.mondia.music.base.states

data class ResponseResult<T>(
    val status: ICommonStatus,
    val data: T?,
    val error: String?,
    @ErrorTypes val errorType: Int?
) : IResult<T> {

    companion object {
        fun <T> loading() = ResponseResult<T>(CommonStatusImp.LOADING, null, null, null)

        fun <T> success() = ResponseResult<T>(CommonStatusImp.SUCCESS, null, null, null)

        fun <T> success(data: T?) = ResponseResult(CommonStatusImp.SUCCESS, data, null, null)

        fun <T> error(error: String?, errorType: Int?) =
            ResponseResult<T>(CommonStatusImp.ERROR, null, error, errorType)
    }

    override fun fetchData(): T? = data

    override fun fetchError(): Pair<Int, String?>? = errorType?.let {
        Pair(it, error)
    }

    override fun whichStatus(): ICommonStatus = status
}
