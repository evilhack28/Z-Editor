package com.example.Z_Editor.views.editor.pages.module

import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.HelpOutline
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.Z_Editor.data.PvzLevelFile
import com.example.Z_Editor.data.RtidParser
import com.example.Z_Editor.data.TidePropertiesData
import com.example.Z_Editor.views.editor.EditorHelpDialog
import com.example.Z_Editor.views.editor.HelpSection
import com.example.Z_Editor.views.editor.NumberInputInt
import com.google.gson.Gson

private val gson = Gson()

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TidePropertiesEP(
    rtid: String,
    onBack: () -> Unit,
    rootLevelFile: PvzLevelFile,
    scrollState: ScrollState
) {
    val focusManager = LocalFocusManager.current
    var showHelpDialog by remember { mutableStateOf(false) }
    val currentAlias = RtidParser.parse(rtid)?.alias ?: "Tide"

    // 数据状态
    val dataState = remember {
        val obj = rootLevelFile.objects.find { it.aliases?.contains(currentAlias) == true }
        val data = try {
            if (obj != null) {
                gson.fromJson(obj.objData, TidePropertiesData::class.java)
            } else {
                TidePropertiesData()
            }
        } catch (_: Exception) {
            TidePropertiesData()
        }
        mutableStateOf(data)
    }

    // 同步函数
    fun sync() {
        val obj = rootLevelFile.objects.find { it.aliases?.contains(currentAlias) == true }
        if (obj != null) {
            obj.objData = gson.toJsonTree(dataState.value)
        }
    }

    Scaffold(
        modifier = Modifier.pointerInput(Unit) {
            detectTapGestures(onTap = { focusManager.clearFocus() })
        },
        topBar = {
            TopAppBar(
                title = { Text("潮水配置", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, "返回", tint = Color.White)
                    }
                },
                actions = {
                    IconButton(onClick = { showHelpDialog = true }) {
                        Icon(Icons.AutoMirrored.Filled.HelpOutline, "帮助说明", tint = Color.White)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color(0xFF0277BD),  // 蓝色主题（代表水）
                    titleContentColor = Color.White,
                    actionIconContentColor = Color.White
                )
            )
        }
    ) { padding ->
        if (showHelpDialog) {
            EditorHelpDialog(
                title = "潮水模块说明",
                onDismiss = { showHelpDialog = false },
                themeColor = Color(0xFF0277BD)
            ) {
                HelpSection(
                    title = "简要介绍",
                    body = "本模块用于开启关卡中的潮水系统，以便后续使用潮水更改事件。"
                )
                HelpSection(
                    title = "初始潮水位置",
                    body = "StartingWaveLocation 指定潮水的初始位置。场地最右边为0，最左边为9。允许输入负数在内的整数。"
                )
            }
        }

        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
                .padding(16.dp)
                .verticalScroll(scrollState),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // 信息提示框
            Card(
                colors = CardDefaults.cardColors(containerColor = Color(0xFFE3F2FD)),  // 浅蓝色背景
                elevation = CardDefaults.cardElevation(2.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier.padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        Icons.Outlined.Info,
                        null,
                        tint = Color(0xFF0277BD),
                        modifier = Modifier.width(24.dp)
                    )
                    Spacer(Modifier.width(12.dp))
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            "场地坐标说明",
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF0277BD),
                            fontSize = 14.sp
                        )
                        Spacer(Modifier.height(4.dp))
                        Text(
                            "场地最右边为0，最左边为9",
                            color = Color(0xFF01579B),
                            fontSize = 13.sp
                        )
                    }
                }
            }

            Text(
                "潮水配置",
                style = MaterialTheme.typography.titleMedium,
                color = Color(0xFF0277BD),
                fontWeight = FontWeight.Bold
            )

            Card(
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(2.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(20.dp)) {
                    Text(
                        "初始潮水位置 (StartingWaveLocation)",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.Gray,
                        modifier = Modifier.padding(bottom = 8.dp)
                    )

                    NumberInputInt(
                        value = dataState.value.startingWaveLocation,
                        onValueChange = {
                            dataState.value = dataState.value.copy(startingWaveLocation = it)
                            sync()
                        },
                        label = "初始位置",
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(Modifier.height(8.dp))

                    Text(
                        "允许输入负数在内的整数。0表示场地最右边，9表示场地最左边。",
                        fontSize = 12.sp,
                        color = Color.Gray,
                        lineHeight = 16.sp
                    )
                }
            }

            Spacer(Modifier.height(32.dp))
        }
    }
}