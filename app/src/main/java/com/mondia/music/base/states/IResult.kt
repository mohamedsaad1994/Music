package com.mondia.music.base.states

interface IResult<T> {

    fun fetchData(): T?

    fun fetchError(): Pair<Int, String?>?

    fun whichStatus(): ICommonStatus
}
