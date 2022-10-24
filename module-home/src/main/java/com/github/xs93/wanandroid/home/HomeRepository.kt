package com.github.xs93.wanandroid.home

import com.github.xs93.core.base.repository.BaseRepository
import com.github.xs93.retrofit.EasyRetrofit
import com.github.xs93.retrofit.model.RequestState
import com.github.xs93.wanandroid.home.model.Banner
import com.orhanobut.logger.Logger
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.onStart

/**
 * 首页数据仓库
 *
 * @author XuShuai
 * @version v1.0
 * @date 2022/10/24 14:18
 * @email 466911254@qq.com
 */
class HomeRepository : BaseRepository() {


    private val homeApi by lazy {
        EasyRetrofit.create(service = HomeApi::class.java)
    }

    /** 获取首页Banner数据 */
    fun getBanner(): Flow<RequestState<List<Banner>>> {
        return flow<RequestState<List<Banner>>> {
            val repo = homeApi.remoteBanner().coverData()
            emit(RequestState.Success(repo))
        }.onStart {
            emit(RequestState.Loading)
        }.catch { e ->
            emit(RequestState.Error(Exception(e)))
        }
    }
}