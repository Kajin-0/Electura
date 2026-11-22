package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Analytics
import androidx.compose.material.icons.filled.Bolt
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.EditNote
import androidx.compose.material.icons.filled.GridView
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.PersonSearch
import androidx.compose.material.icons.outlined.Analytics
import androidx.compose.material.icons.outlined.EditNote
import androidx.compose.material.icons.outlined.GridView
import androidx.compose.material.icons.outlined.PersonSearch
import androidx.compose.material3.AlertDialog
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
import androidx.compose.material3.TextButton
import androidx.compose.material3.minimumInteractiveComponentSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.model.AppTab
import com.example.model.AttemptRecord
import com.example.ui.components.CircuitTag
import com.example.ui.screens.GuideScreen
import com.example.ui.screens.IndexScreen
import com.example.ui.screens.SolveScreen
import com.example.ui.screens.StatsScreen
import com.example.ui.theme.ErrorRed
import com.example.ui.theme.InkBlack
import com.example.ui.theme.JetBrainsMonoFontFamily
import com.example.ui.theme.MyApplicationTheme
import com.example.ui.theme.OrangeAccent
import com.example.ui.theme.OrangeSoftBg
import com.example.ui.theme.SlateDark
import com.example.ui.theme.SlateMuted
import com.example.ui.theme.SubtleBorder
import com.example.ui.theme.SuccessGreen
import com.example.ui.theme.SurfaceOffWhite
import com.example.ui.theme.SurfaceWhite
import com.example.viewmodel.CircuitGameUiState
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

    BackHandler(enabled = state.showHistorySheet || state.activeTab != AppTab.INDEX) {
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
                            onClick = { viewModel.selectTab(tab) },
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
                // Minimal Top Bar
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .windowInsetsPadding(WindowInsets.statusBars)
                        .padding(horizontal = 18.dp, vertical = 8.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    if (state.activeTab != AppTab.INDEX) {
                        IconButton(
                            onClick = {
                                if (state.activeTab == AppTab.SOLVE) {
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
                    } else {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.padding(start = 6.dp)
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(24.dp)
                                    .background(OrangeSoftBg, RoundedCornerShape(7.dp))
                                    .border(1.dp, OrangeAccent.copy(alpha = 0.25f), RoundedCornerShape(7.dp)),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    Icons.Filled.Bolt,
                                    contentDescription = null,
                                    tint = OrangeAccent,
                                    modifier = Modifier.size(14.dp)
                                )
                            }
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "Electura",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold,
                                color = InkBlack
                            )
                        }
                    }

                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(2.dp)
                    ) {
                        IconButton(
                            onClick = { viewModel.setHistorySheetVisible(true) },
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
                                onClick = { menuExpanded = true },
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
                                        menuExpanded = false
                                        viewModel.setResetConfirmDialogVisible(true)
                                    }
                                )
                            }
                        }
                    }
                }

                // Active Screen Content
                Box(modifier = Modifier.weight(1f)) {
                    when (state.activeTab) {
                        AppTab.INDEX -> IndexScreen(
                            state = state,
                            onSelectProblem = viewModel::selectProblem,
                            onStartDrill = viewModel::startRandomDrill,
                            onSelectCategory = viewModel::setCategoryFilter,
                            onToggleUnsolvedOnly = viewModel::toggleFilterUnsolvedOnly,
                            onSelectDifficulty = viewModel::setDifficultyFilter
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
                            onReturnToOriginal = viewModel::returnToOriginalProblem
                        )
                        AppTab.STATS -> StatsScreen(
                            state = state,
                            onRequestReset = { viewModel.setResetConfirmDialogVisible(true) }
                        )
                        AppTab.GUIDE -> GuideScreen()
                    }
                }

                // Bottom Bar on Compact Screens with Navigation Bar Insets
                if (!useRail) {
                    MinimalBottomBar(
                        activeTab = state.activeTab,
                        onSelectTab = viewModel::selectTab
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
        AlertDialog(
            onDismissRequest = { viewModel.setResetConfirmDialogVisible(false) },
            containerColor = SurfaceWhite,
            shape = RoundedCornerShape(20.dp),
            title = {
                Text(
                    "Reset Progress?",
                    style = MaterialTheme.typography.titleLarge,
                    color = InkBlack
                )
            },
            text = {
                Text(
                    "This clears your solved problems, XP, streaks, and accuracy telemetry.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = SlateDark
                )
            },
            confirmButton = {
                TextButton(onClick = { viewModel.confirmResetProgress() }) {
                    Text("Reset", fontWeight = FontWeight.Bold, color = ErrorRed)
                }
            },
            dismissButton = {
                TextButton(onClick = { viewModel.setResetConfirmDialogVisible(false) }) {
                    Text("Cancel", fontWeight = FontWeight.SemiBold, color = SlateDark)
                }
            }
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
            .background(SurfaceWhite)
            .windowInsetsPadding(WindowInsets.navigationBars)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(1.dp)
                .background(SubtleBorder)
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
                val color = if (isActive) OrangeAccent else SlateMuted
                Column(
                    modifier = Modifier
                        .minimumInteractiveComponentSize()
                        .clip(RoundedCornerShape(14.dp))
                        .clickable { onSelectTab(tab) }
                        .padding(horizontal = 12.dp, vertical = 4.dp)
                        .testTag("nav_tab_${tab.name.lowercase()}"),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(50))
                            .background(if (isActive) OrangeSoftBg else SurfaceWhite)
                            .padding(horizontal = 14.dp, vertical = 4.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = tabIcon(tab, isActive),
                            contentDescription = tab.label,
                            tint = color,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                    Text(
                        text = tab.label,
                        fontSize = 11.sp,
                        fontWeight = if (isActive) FontWeight.SemiBold else FontWeight.Normal,
                        color = if (isActive) InkBlack else SlateMuted,
                        modifier = Modifier.padding(top = 2.dp)
                    )
                }
            }
        }
    }
}

@Composable
private fun AttemptHistoryDialog(
    state: CircuitGameUiState,
    onDismiss: () -> Unit,
    onSelectAttempt: (AttemptRecord) -> Unit
) {
    Dialog(onDismissRequest = onDismiss) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(SurfaceWhite, RoundedCornerShape(22.dp))
                .border(1.dp, SubtleBorder, RoundedCornerShape(22.dp))
                .padding(22.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                CircuitTag(text = "Session Log")
                IconButton(
                    onClick = onDismiss,
                    modifier = Modifier.minimumInteractiveComponentSize()
                ) {
                    Icon(
                        Icons.Filled.Close,
                        contentDescription = "Close history",
                        tint = SlateDark,
                        modifier = Modifier.size(20.dp)
                    )
                }
            }

            Text(
                text = "Recent Attempts",
                style = MaterialTheme.typography.headlineMedium,
                color = InkBlack
            )
            Spacer(modifier = Modifier.height(14.dp))

            if (state.recentAttempts.isEmpty()) {
                Text(
                    text = "No submissions yet. Solve any problem to record your answers here.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = SlateMuted,
                    modifier = Modifier.padding(vertical = 16.dp)
                )
            } else {
                LazyColumn(
                    modifier = Modifier.height(280.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(state.recentAttempts) { item ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(14.dp))
                                .border(1.dp, SubtleBorder, RoundedCornerShape(14.dp))
                                .clickable { onSelectAttempt(item) }
                                .padding(horizontal = 14.dp, vertical = 12.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = "${item.problemCode} · ${item.problemTitle}",
                                    fontSize = 13.sp,
                                    fontWeight = FontWeight.SemiBold,
                                    color = InkBlack,
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis
                                )
                                Text(
                                    text = item.submittedAnswer,
                                    fontFamily = JetBrainsMonoFontFamily,
                                    fontSize = 11.5.sp,
                                    color = SlateMuted,
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis
                                )
                            }
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = if (item.isCorrect) "+${item.xpEarned} XP" else "Retry",
                                fontFamily = JetBrainsMonoFontFamily,
                                fontSize = 11.5.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = if (item.isCorrect) SuccessGreen else ErrorRed
                            )
                        }
                    }
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
