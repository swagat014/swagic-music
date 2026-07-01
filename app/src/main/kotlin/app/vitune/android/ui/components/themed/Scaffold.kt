package app.vitune.android.ui.components.themed

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedContentTransitionScope.SlideDirection.Companion.Down
import androidx.compose.animation.AnimatedContentTransitionScope.SlideDirection.Companion.Up
import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.ContentTransform
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.VisibilityThreshold
import androidx.compose.animation.core.spring
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.IntOffset
import app.vitune.android.R
import app.vitune.android.preferences.UIStatePreferences
import app.vitune.core.ui.LocalAppearance
import kotlinx.collections.immutable.toImmutableList

import androidx.compose.foundation.clickable
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.BasicText
import androidx.compose.ui.Alignment
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import app.vitune.android.utils.bold
import app.vitune.core.ui.utils.isLandscape

@Composable
fun Scaffold(
    key: String,
    topIconButtonId: Int,
    onTopIconButtonClick: () -> Unit,
    tabIndex: Int,
    onTabChange: (Int) -> Unit,
    tabColumnContent: TabsBuilder.() -> Unit,
    modifier: Modifier = Modifier,
    tabsEditingTitle: String = stringResource(R.string.tabs),
    content: @Composable AnimatedVisibilityScope.(Int) -> Unit
) {
    val (colorPalette, typography) = LocalAppearance.current
    var hiddenTabs by UIStatePreferences.mutableTabStateOf(key)
    val isLandscape = isLandscape

    if (isLandscape) {
        Row(
            modifier = modifier
                .background(colorPalette.background0)
                .fillMaxSize()
        ) {
            NavigationRail(
                topIconButtonId = topIconButtonId,
                onTopIconButtonClick = onTopIconButtonClick,
                tabIndex = tabIndex,
                onTabIndexChange = onTabChange,
                hiddenTabs = hiddenTabs,
                setHiddenTabs = { hiddenTabs = it.toImmutableList() },
                tabsEditingTitle = tabsEditingTitle,
                content = tabColumnContent
            )

            Box(modifier = Modifier.weight(1f).fillMaxSize()) {
                AnimatedContent(
                    targetState = tabIndex,
                    transitionSpec = {
                        val slideDirection = if (targetState > initialState) Up else Down
                        val animationSpec = spring(
                            dampingRatio = 0.9f,
                            stiffness = Spring.StiffnessLow,
                            visibilityThreshold = IntOffset.VisibilityThreshold
                        )

                        ContentTransform(
                            targetContentEnter = slideIntoContainer(slideDirection, animationSpec),
                            initialContentExit = slideOutOfContainer(slideDirection, animationSpec),
                            sizeTransform = null
                        )
                    },
                    content = content,
                    label = ""
                )
            }
        }
    } else {
        Box(
            modifier = modifier
                .background(colorPalette.background0)
                .fillMaxSize()
        ) {
            Column(modifier = Modifier.fillMaxSize()) {
                // Modern Top Header Row for Brand Identity
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 20.dp, end = 20.dp, top = 28.dp, bottom = 12.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Image(
                            painter = painterResource(R.mipmap.ic_launcher_round),
                            contentDescription = null,
                            modifier = Modifier.size(34.dp)
                        )
                        Spacer(modifier = Modifier.width(10.dp))
                        BasicText(
                            text = "Swagic Music",
                            style = typography.m.bold.copy(color = colorPalette.text)
                        )
                    }

                    Image(
                        painter = painterResource(topIconButtonId),
                        contentDescription = null,
                        colorFilter = ColorFilter.tint(colorPalette.textSecondary),
                        modifier = Modifier
                            .clip(CircleShape)
                            .clickable(onClick = onTopIconButtonClick)
                            .padding(8.dp)
                            .size(24.dp)
                    )
                }

                Box(modifier = Modifier.weight(1f).fillMaxSize()) {
                    AnimatedContent(
                        targetState = tabIndex,
                        transitionSpec = {
                            val slideDirection = if (targetState > initialState) Up else Down
                            val animationSpec = spring(
                                dampingRatio = 0.9f,
                                stiffness = Spring.StiffnessLow,
                                visibilityThreshold = IntOffset.VisibilityThreshold
                            )

                            ContentTransform(
                                targetContentEnter = slideIntoContainer(slideDirection, animationSpec),
                                initialContentExit = slideOutOfContainer(slideDirection, animationSpec),
                                sizeTransform = null
                            )
                        },
                        content = content,
                        label = ""
                    )
                }

                Spacer(modifier = Modifier.height(78.dp))
            }

            NavigationRail(
                topIconButtonId = topIconButtonId,
                onTopIconButtonClick = onTopIconButtonClick,
                tabIndex = tabIndex,
                onTabIndexChange = onTabChange,
                hiddenTabs = hiddenTabs,
                setHiddenTabs = { hiddenTabs = it.toImmutableList() },
                tabsEditingTitle = tabsEditingTitle,
                content = tabColumnContent,
                modifier = Modifier.align(Alignment.BottomCenter)
            )
        }
    }
}
