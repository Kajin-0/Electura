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
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.data.ProblemBank
import com.example.model.AppTab
import com.example.ui.components.BrutalistTag
import com.example.ui.components.brutalShadow
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
                        NavigationRailItem(
                            selected = state.activeTab == tab,
                            onClick = { viewModel.selectTab(tab) },
                            icon = {
                                Icon(
                                    imageVector = tabIcon(tab),
                                    contentDescription = tab.label
                                )
                            },
                            label = {
                                Text(
                                    text = tab.label,
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            },
                            colors = NavigationRailItemDefaults.colors(
                                selectedIconColor = OrangeAccent,
                                selectedTextColor = OrangeAccent,
                                indicatorColor = OrangeSoftBg,
                                unselectedIconColor = InkBlack.copy(alpha = 0.45f),
                                unselectedTextColor = InkBlack.copy(alpha = 0.45f)
                            )
                        )
                    }
                }
            }

            Column(modifier = Modifier.fillMaxSize()) {
                // Top Bar with Status Bar Safe Area
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .windowInsetsPadding(WindowInsets.statusBars)
                        .padding(horizontal = 16.dp, vertical = 10.dp),
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
                                modifier = Modifier.size(26.dp)
                            )
                        }
                    } else {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.padding(start = 8.dp)
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(28.dp)
                                    .background(OrangeAccent, RoundedCornerShape(8.dp)),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    Icons.Filled.Bolt,
                                    contentDescription = null,
                                    tint = SurfaceWhite,
                                    modifier = Modifier.size(18.dp)
                                )
                            }
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "AC LAB",
                                fontWeight = FontWeight.Black,
                                fontSize = 15.sp,
                                letterSpacing = 1.sp,
                                color = InkBlack
                            )
                        }
                    }

                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
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
                                tint = InkBlack,
                                modifier = Modifier.size(24.dp)
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
                                    tint = InkBlack,
                                    modifier = Modifier.size(24.dp)
                                )
                            }

                            DropdownMenu(
                                expanded = menuExpanded,
                                onDismissRequest = { menuExpanded = false },
                                modifier = Modifier
                                    .background(SurfaceWhite)
                                    .border(1.5.dp, InkBlack, RoundedCornerShape(10.dp))
                            ) {
                                DropdownMenuItem(
                                    text = {
                                        Text(
                                            "New Random Drill",
                                            fontWeight = FontWeight.Bold,
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
                                            if (state.progress.showQuickKeypad) "Hide Math Symbol Bar" else "Show Math Symbol Bar",
                                            fontWeight = FontWeight.Medium,
                                            color = InkBlack
                                        )
                                    },
                                    onClick = {
                                        menuExpanded = false
                                        viewModel.toggleQuickKeypad()
                                    }
                                )
                                DropdownMenuItem(
                                    text = {
                                        Text(
                                            "Reset Progress",
                                            fontWeight = FontWeight.Bold,
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
                            onNewDrill = { viewModel.startRandomDrill(state.currentProblem.category) }
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
                    BrutalistBottomBar(
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
            onSelectProblemById = { id ->
                ProblemBank.curatedProblems.find { it.id == id }?.let {
                    viewModel.selectProblem(it)
                    viewModel.setHistorySheetVisible(false)
                }
            }
        )
    }

    if (state.showResetConfirmDialog) {
        AlertDialog(
            onDismissRequest = { viewModel.setResetConfirmDialogVisible(false) },
            containerColor = SurfaceWhite,
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
                    Text("RESET", fontWeight = FontWeight.Black, color = ErrorRed)
                }
            },
            dismissButton = {
                TextButton(onClick = { viewModel.setResetConfirmDialogVisible(false) }) {
                    Text("CANCEL", fontWeight = FontWeight.Bold, color = InkBlack)
                }
            }
        )
    }
}

@Composable
private fun BrutalistBottomBar(
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
                .padding(vertical = 6.dp, horizontal = 12.dp),
            horizontalArrangement = Arrangement.SpaceAround,
            verticalAlignment = Alignment.CenterVertically
        ) {
            AppTab.entries.forEach { tab ->
                val isActive = activeTab == tab
                val color = if (isActive) OrangeAccent else InkBlack.copy(alpha = 0.42f)
                Column(
                    modifier = Modifier
                        .minimumInteractiveComponentSize()
                        .clip(RoundedCornerShape(14.dp))
                        .background(if (isActive) OrangeSoftBg else SurfaceWhite)
                        .clickable { onSelectTab(tab) }
                        .padding(horizontal = 16.dp, vertical = 6.dp)
                        .testTag("nav_tab_${tab.name.lowercase()}"),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Icon(
                        imageVector = tabIcon(tab),
                        contentDescription = tab.label,
                        tint = color,
                        modifier = Modifier.size(22.dp)
                    )
                    Text(
                        text = tab.label,
                        fontSize = 10.sp,
                        fontWeight = if (isActive) FontWeight.Bold else FontWeight.Medium,
                        color = color,
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
    onSelectProblemById: (String) -> Unit
) {
    Dialog(onDismissRequest = onDismiss) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(SurfaceWhite, RoundedCornerShape(22.dp))
                .border(1.5.dp, InkBlack, RoundedCornerShape(22.dp))
                .padding(20.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                BrutalistTag(text = "SESSION LOG")
                IconButton(
                    onClick = onDismiss,
                    modifier = Modifier.minimumInteractiveComponentSize()
                ) {
                    Icon(Icons.Filled.Close, contentDescription = "Close history", tint = InkBlack)
                }
            }

            Text(
                text = "Attempt History",
                style = MaterialTheme.typography.headlineMedium,
                color = InkBlack
            )
            Spacer(modifier = Modifier.height(12.dp))

            if (state.recentAttempts.isEmpty()) {
                Text(
                    text = "No submissions in this session yet. Solve a circuit problem to record telemetry.",
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
                                .clip(RoundedCornerShape(12.dp))
                                .border(1.dp, SubtleBorder, RoundedCornerShape(12.dp))
                                .clickable { onSelectProblemById(item.problemId) }
                                .padding(12.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = "${item.problemCode} • ${item.problemTitle}",
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = InkBlack
                                )
                                Text(
                                    text = "Answer: ${item.submittedAnswer}",
                                    fontFamily = JetBrainsMonoFontFamily,
                                    fontSize = 11.sp,
                                    color = SlateDark
                                )
                            }
                            Text(
                                text = if (item.isCorrect) "+${item.xpEarned} XP" else "RETRY",
                                fontFamily = JetBrainsMonoFontFamily,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = if (item.isCorrect) SuccessGreen else ErrorRed
                            )
                        }
                    }
                }
            }
        }
    }
}

private fun tabIcon(tab: AppTab): ImageVector = when (tab) {
    AppTab.INDEX -> Icons.Filled.GridView
    AppTab.SOLVE -> Icons.Filled.EditNote
    AppTab.STATS -> Icons.Filled.Analytics
    AppTab.GUIDE -> Icons.Filled.PersonSearch
}
