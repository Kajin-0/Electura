package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Analytics
import androidx.compose.material.icons.filled.Bolt
import androidx.compose.material.icons.filled.EditNote
import androidx.compose.material.icons.filled.GridView
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.PersonSearch
import androidx.compose.material.icons.outlined.Analytics
import androidx.compose.material.icons.outlined.EditNote
import androidx.compose.material.icons.outlined.GridView
import androidx.compose.material.icons.outlined.PersonSearch
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationRail
import androidx.compose.material3.NavigationRailItem
import androidx.compose.material3.NavigationRailItemDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.minimumInteractiveComponentSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.model.AppTab
import com.example.ui.components.AttemptHistoryDialog
import com.example.ui.components.ResetProgressDialog
import com.example.ui.components.tactileClickable
import com.example.ui.components.tickLight
import com.example.ui.screens.GuideScreen
import com.example.ui.screens.IndexScreen
import com.example.ui.screens.SolveScreen
import com.example.ui.screens.StatsScreen
import com.example.ui.theme.CopperBorderSoft
import com.example.ui.theme.ErrorRed
import com.example.ui.theme.HairlineBorder
import com.example.ui.theme.InkBlack
import com.example.ui.theme.JetBrainsMonoFontFamily
import com.example.ui.theme.MyApplicationTheme
import com.example.ui.theme.OrangeAccent
import com.example.ui.theme.OrangeSoftBg
import com.example.ui.theme.PaperShadowTint
import com.example.ui.theme.SlateDark
import com.example.ui.theme.SlateMuted
import com.example.ui.theme.SubtleBorder
import com.example.ui.theme.SurfaceOffWhite
import com.example.ui.theme.SurfaceWhite
import com.example.viewmodel.CircuitGameViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MyApplicationTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = SurfaceOffWhite
                ) {
                    CircuitAppRoot()
                }
            }
        }
    }
}

@Composable
fun CircuitAppRoot(
    viewModel: CircuitGameViewModel = viewModel()
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    var menuExpanded by remember { mutableStateOf(false) }
    var isContentScrolled by remember { mutableStateOf(false) }
    val haptic = LocalHapticFeedback.current
    val keyboardController = LocalSoftwareKeyboardController.current
    val navigateToTab: (AppTab) -> Unit = { tab ->
        keyboardController?.hide()
        viewModel.selectTab(tab)
    }

    LaunchedEffect(state.activeTab) {
        isContentScrolled = false
    }

    BackHandler(enabled = state.showHistorySheet || state.activeTab != AppTab.INDEX) {
        keyboardController?.hide()
        when {
            state.showHistorySheet -> viewModel.setHistorySheetVisible(false)
            state.activeTab == AppTab.SOLVE -> viewModel.onBackPressedInSolve()
            else -> viewModel.selectTab(AppTab.INDEX)
        }
    }

    BoxWithConstraints(
        modifier = Modifier
            .fillMaxSize()
            .background(SurfaceOffWhite)
    ) {
        val useRail = maxWidth >= 640.dp

        Row(modifier = Modifier.fillMaxSize()) {
            if (useRail) {
                NavigationRail(
                    modifier = Modifier
                        .fillMaxHeight()
                        .windowInsetsPadding(WindowInsets.statusBars)
                        .windowInsetsPadding(WindowInsets.navigationBars)
                        .border(1.dp, SubtleBorder),
                    containerColor = SurfaceWhite
                ) {
                    Spacer(modifier = Modifier.height(12.dp))
                    AppTab.entries.forEach { tab ->
                        val selected = state.activeTab == tab
                        NavigationRailItem(
                            selected = selected,
                            onClick = {
                                haptic.tickLight()
                                navigateToTab(tab)
                            },
                            icon = {
                                Icon(
                                    imageVector = tabIcon(tab, selected),
                                    contentDescription = tab.label
                                )
                            },
                            label = {
                                Text(
                                    text = tab.label,
                                    fontSize = 11.sp,
                                    fontWeight = if (selected) FontWeight.SemiBold else FontWeight.Medium
                                )
                            },
                            colors = NavigationRailItemDefaults.colors(
                                selectedIconColor = OrangeAccent,
                                selectedTextColor = OrangeAccent,
                                indicatorColor = OrangeSoftBg,
                                unselectedIconColor = SlateMuted,
                                unselectedTextColor = SlateMuted
                            )
                        )
                    }
                }
            }

            Column(modifier = Modifier.fillMaxSize()) {
                // Minimal Top Bar with Animated Contextual Brand/Back Crossfade
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .windowInsetsPadding(WindowInsets.statusBars)
                        .padding(horizontal = 18.dp, vertical = 8.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    AnimatedContent(
                        targetState = state.activeTab,
                        transitionSpec = {
                            fadeIn(tween(180)).togetherWith(fadeOut(tween(140)))
                        },
                        label = "topBarLeading"
                    ) { tab ->
                        if (tab != AppTab.INDEX) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                IconButton(
                                    onClick = {
                                        haptic.tickLight()
                                        keyboardController?.hide()
                                        if (tab == AppTab.SOLVE) {
                                            viewModel.onBackPressedInSolve()
                                        } else {
                                            viewModel.selectTab(AppTab.INDEX)
                                        }
                                    },
                                    modifier = Modifier
                                        .minimumInteractiveComponentSize()
                                        .testTag("top_back_button")
                                ) {
                                    Icon(
                                        Icons.AutoMirrored.Filled.ArrowBack,
                                        contentDescription = stringResource(R.string.cd_back),
                                        tint = InkBlack,
                                        modifier = Modifier.size(22.dp)
                                    )
                                }
                                Text(
                                    text = tab.label.uppercase(),
                                    fontFamily = JetBrainsMonoFontFamily,
                                    fontSize = 10.5.sp,
                                    fontWeight = FontWeight.SemiBold,
                                    color = SlateMuted,
                                    letterSpacing = 0.8.sp
                                )
                            }
                        } else {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier.padding(start = 6.dp)
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(26.dp)
                                        .background(OrangeSoftBg, RoundedCornerShape(8.dp))
                                        .border(1.dp, CopperBorderSoft, RoundedCornerShape(8.dp)),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(
                                        Icons.Filled.Bolt,
                                        contentDescription = null,
                                        tint = OrangeAccent,
                                        modifier = Modifier.size(15.dp)
                                    )
                                }
                                Spacer(modifier = Modifier.width(9.dp))
                                Text(
                                    text = "Electura",
                                    style = MaterialTheme.typography.titleMedium,
                                    fontWeight = FontWeight.Bold,
                                    color = InkBlack
                                )
                            }
                        }
                    }

                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(2.dp)
                    ) {
                        IconButton(
                            onClick = {
                                haptic.tickLight()
                                keyboardController?.hide()
                                viewModel.setHistorySheetVisible(true)
                            },
                            modifier = Modifier
                                .minimumInteractiveComponentSize()
                                .testTag("history_button")
                        ) {
                            Icon(
                                Icons.Filled.History,
                                contentDescription = stringResource(R.string.cd_history),
                                tint = SlateDark,
                                modifier = Modifier.size(21.dp)
                            )
                        }

                        Box {
                            IconButton(
                                onClick = {
                                    haptic.tickLight()
                                    keyboardController?.hide()
                                    menuExpanded = true
                                },
                                modifier = Modifier
                                    .minimumInteractiveComponentSize()
                                    .testTag("more_options_button")
                            ) {
                                Icon(
                                    Icons.Filled.MoreVert,
                                    contentDescription = stringResource(R.string.cd_more),
                                    tint = SlateDark,
                                    modifier = Modifier.size(21.dp)
                                )
                            }

                            DropdownMenu(
                                expanded = menuExpanded,
                                onDismissRequest = { menuExpanded = false },
                                modifier = Modifier
                                    .background(SurfaceWhite, RoundedCornerShape(14.dp))
                                    .border(1.dp, SubtleBorder, RoundedCornerShape(14.dp))
                            ) {
                                DropdownMenuItem(
                                    text = {
                                        Text(
                                            "New Random Drill",
                                            fontWeight = FontWeight.SemiBold,
                                            color = InkBlack
                                        )
                                    },
                                    onClick = {
                                        haptic.tickLight()
                                        menuExpanded = false
                                        viewModel.startRandomDrill()
                                    }
                                )
                                DropdownMenuItem(
                                    text = {
                                        Text(
                                            if (state.progress.showQuickKeypad) "Hide Symbol Bar" else "Show Symbol Bar",
                                            fontWeight = FontWeight.Normal,
                                            color = SlateDark
                                        )
                                    },
                                    onClick = {
                                        haptic.tickLight()
                                        menuExpanded = false
                                        viewModel.toggleQuickKeypad()
                                    }
                                )
                                HorizontalDivider(color = SubtleBorder, thickness = 0.5.dp)
                                DropdownMenuItem(
                                    text = {
                                        Text(
                                            "Reset Progress",
                                            fontWeight = FontWeight.SemiBold,
                                            color = ErrorRed
                                        )
                                    },
                                    onClick = {
                                        haptic.tickLight()
                                        menuExpanded = false
                                        viewModel.setResetConfirmDialogVisible(true)
                                    }
                                )
                            }
                        }
                    }
                }

                val topDividerAlpha by animateFloatAsState(
                    targetValue = if (isContentScrolled) 1f else 0f,
                    animationSpec = tween(180),
                    label = "topBarDividerAlpha"
                )
                HorizontalDivider(
                    color = HairlineBorder.copy(alpha = topDividerAlpha),
                    thickness = 0.85.dp
                )

                // Directional Screen Router
                Box(modifier = Modifier.weight(1f)) {
                    AnimatedContent(
                        targetState = state.activeTab,
                        transitionSpec = {
                            val forward = targetState.ordinal > initialState.ordinal
                            val enterSlide = slideInHorizontally(tween(240)) { width ->
                                if (forward) width / 7 else -width / 7
                            } + fadeIn(tween(220))
                            val exitSlide = slideOutHorizontally(tween(180)) { width ->
                                if (forward) -width / 9 else width / 9
                            } + fadeOut(tween(150))
                            enterSlide.togetherWith(exitSlide)
                        },
                        label = "tabScreenRouter"
                    ) { activeTab ->
                        when (activeTab) {
                            AppTab.INDEX -> IndexScreen(
                                state = state,
                                onSelectProblem = viewModel::selectProblem,
                                onStartDrill = viewModel::startRandomDrill,
                                onSelectCategory = viewModel::setCategoryFilter,
                                onToggleUnsolvedOnly = viewModel::toggleFilterUnsolvedOnly,
                                onSelectDifficulty = viewModel::setDifficultyFilter,
                                onScrolledChange = { isContentScrolled = it }
                            )
                            AppTab.SOLVE -> SolveScreen(
                                state = state,
                                onInputChange = viewModel::updateUserInput,
                                onAppendSymbol = viewModel::appendSymbol,
                                onSubmit = viewModel::submitAnswer,
                                onRevealHint = viewModel::revealNextHint,
                                onToggleSteps = viewModel::toggleSolutionSteps,
                                onNextProblem = viewModel::goToNextProblem,
                                onPracticeVariation = { viewModel.startVariation() },
                                onReturnToOriginal = viewModel::returnToOriginalProblem,
                                onScrolledChange = { isContentScrolled = it }
                            )
                            AppTab.STATS -> StatsScreen(
                                state = state,
                                onRequestReset = { viewModel.setResetConfirmDialogVisible(true) },
                                onSelectAttempt = viewModel::reopenAttempt,
                                onScrolledChange = { isContentScrolled = it }
                            )
                            AppTab.GUIDE -> GuideScreen(
                                onScrolledChange = { isContentScrolled = it }
                            )
                        }
                    }
                }

                // Animated Bottom Bar on Compact Screens with Navigation Bar Insets
                if (!useRail) {
                    MinimalBottomBar(
                        activeTab = state.activeTab,
                        onSelectTab = navigateToTab
                    )
                }
            }
        }
    }

    if (state.showHistorySheet) {
        AttemptHistoryDialog(
            state = state,
            onDismiss = { viewModel.setHistorySheetVisible(false) },
            onSelectAttempt = viewModel::reopenAttempt
        )
    }

    if (state.showResetConfirmDialog) {
        ResetProgressDialog(
            onDismiss = { viewModel.setResetConfirmDialogVisible(false) },
            onConfirmReset = { viewModel.confirmResetProgress() }
        )
    }
}

@Composable
private fun MinimalBottomBar(
    activeTab: AppTab,
    onSelectTab: (AppTab) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .shadow(
                elevation = 6.dp,
                clip = false,
                ambientColor = PaperShadowTint.copy(alpha = 0.16f),
                spotColor = PaperShadowTint.copy(alpha = 0.12f)
            )
            .background(SurfaceWhite)
            .windowInsetsPadding(WindowInsets.navigationBars)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(0.85.dp)
                .background(HairlineBorder)
        )
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp, horizontal = 12.dp),
            horizontalArrangement = Arrangement.SpaceAround,
            verticalAlignment = Alignment.CenterVertically
        ) {
            AppTab.entries.forEach { tab ->
                val isActive = activeTab == tab
                val iconTint by animateColorAsState(
                    targetValue = if (isActive) OrangeAccent else SlateMuted,
                    animationSpec = tween(200),
                    label = "navTint_${tab.name}"
                )
                val pillBg by animateColorAsState(
                    targetValue = if (isActive) OrangeSoftBg else Color.Transparent,
                    animationSpec = tween(200),
                    label = "navPillBg_${tab.name}"
                )
                val pillWidth by animateDpAsState(
                    targetValue = if (isActive) 50.dp else 32.dp,
                    animationSpec = spring(dampingRatio = 0.72f, stiffness = Spring.StiffnessMediumLow),
                    label = "navPillWidth_${tab.name}"
                )
                val iconScale by animateFloatAsState(
                    targetValue = if (isActive) 1.08f else 1f,
                    animationSpec = spring(dampingRatio = 0.6f, stiffness = Spring.StiffnessMedium),
                    label = "navIconScale_${tab.name}"
                )
                val labelColor by animateColorAsState(
                    targetValue = if (isActive) InkBlack else SlateMuted,
                    animationSpec = tween(200),
                    label = "navLabelColor_${tab.name}"
                )

                Column(
                    modifier = Modifier
                        .minimumInteractiveComponentSize()
                        .clip(RoundedCornerShape(14.dp))
                        .tactileClickable(pressedScale = 0.94f) { onSelectTab(tab) }
                        .padding(horizontal = 10.dp, vertical = 4.dp)
                        .testTag("nav_tab_${tab.name.lowercase()}"),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Box(
                        modifier = Modifier
                            .width(pillWidth)
                            .height(28.dp)
                            .clip(RoundedCornerShape(50))
                            .background(pillBg),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = tabIcon(tab, isActive),
                            contentDescription = tab.label,
                            tint = iconTint,
                            modifier = Modifier
                                .size(20.dp)
                                .graphicsLayer {
                                    scaleX = iconScale
                                    scaleY = iconScale
                                }
                        )
                    }
                    Text(
                        text = tab.label,
                        fontSize = 11.sp,
                        fontWeight = if (isActive) FontWeight.SemiBold else FontWeight.Normal,
                        color = labelColor,
                        modifier = Modifier.padding(top = 2.dp)
                    )
                }
            }
        }
    }
}

private fun tabIcon(tab: AppTab, selected: Boolean): ImageVector = when (tab) {
    AppTab.INDEX -> if (selected) Icons.Filled.GridView else Icons.Outlined.GridView
    AppTab.SOLVE -> if (selected) Icons.Filled.EditNote else Icons.Outlined.EditNote
    AppTab.STATS -> if (selected) Icons.Filled.Analytics else Icons.Outlined.Analytics
    AppTab.GUIDE -> if (selected) Icons.Filled.PersonSearch else Icons.Outlined.PersonSearch
}
