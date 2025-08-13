package com.arvifox.arvi.uicompose.vertpager

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.PageSize
import androidx.compose.foundation.pager.PagerDefaults
import androidx.compose.foundation.pager.VerticalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.arvifox.arvi.R
import kotlinx.serialization.Serializable

@Serializable
object VertPagerNav

@Serializable
private object VertPagerScreen

fun NavGraphBuilder.VertPager(nhc: NavHostController) {
    this.navigation<VertPagerNav>(startDestination = VertPagerScreen) {
        composable<VertPagerScreen> {
            val pagerState = rememberPagerState(
                initialPage = 0,
                pageCount = { items.size },
            )
            Box(
                modifier = Modifier.fillMaxSize()
            ) {
                VerticalPager(
                    state = pagerState,
                    modifier = Modifier.fillMaxWidth(),
                    userScrollEnabled = true,
                    flingBehavior = PagerDefaults.flingBehavior(
                        state = pagerState,
                        snapPositionalThreshold = 0.05f,
                    ),
                    pageSize = PageSize.Fill,
                    beyondViewportPageCount = 1,
                ) {
                    val ss = rememberScrollState()
                    PagerItem(
                        modifier = Modifier
                            .fillMaxSize()
                            .verticalScroll(ss),
                        s = items[it],
                    )
                }
            }
        }
    }
}

@Composable
private fun PagerItem(
    modifier: Modifier = Modifier,
    s: String,
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Image(
            modifier = Modifier
                .fillMaxWidth(0.5f)
                .aspectRatio(1f),
            painter = painterResource(R.drawable.ic_heart_white_60dp),
            contentDescription = null,
        )
        Text(
            text = s,
            fontSize = 34.sp,
        )
        Image(
            modifier = Modifier
                .fillMaxWidth(0.5f)
                .aspectRatio(1f),
            painter = painterResource(R.drawable.ic_untitled_text),
            contentDescription = null,
        )
        Text(
            text = "scroll",
            modifier = Modifier.fillMaxWidth().padding(all = 18.dp),
            fontSize = 16.sp,
        )
    }
}