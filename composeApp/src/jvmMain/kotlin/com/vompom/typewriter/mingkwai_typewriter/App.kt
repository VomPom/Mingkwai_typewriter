package com.vompom.typewriter.mingkwai_typewriter

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeContentPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.text.TextLayoutResult
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.vompom.typewriter.mingkwai_typewriter.data.MainViewModel
import com.vompom.typewriter.mingkwai_typewriter.data.MainViewModel.AudioType
import com.vompom.typewriter.mingkwai_typewriter.data.SnapshotState
import com.vompom.typewriter.mingkwai_typewriter.ext.pxToDp
import com.vompom.typewriter.mingkwai_typewriter.ext.snapshot
import com.vompom.typewriter.mingkwai_typewriter.utils.snapshotDir
import kotlinx.coroutines.delay
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.jetbrains.skia.PictureRecorder

@Composable
@Preview
fun App(viewModel: MainViewModel) {
    MaterialTheme {
        Column(
            modifier = Modifier
                .safeContentPadding()
                .wrapContentHeight()
                .fillMaxWidth(),

            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Typewriter(viewModel)
        }
    }
}

@Composable
fun Typewriter(viewModel: MainViewModel) {
    val mainBackground = Color(239, 229, 219)
    val bottomHeight = 160
    var maxWidth by remember { mutableStateOf(0) }
    var editText by remember { mutableStateOf("") }
    var typedChar by remember { mutableStateOf(' ') }
    val lazyListState = rememberLazyListState()
    val pictureRecorder = PictureRecorder()
    Box(
        modifier = Modifier
            .wrapContentHeight()
            .fillMaxHeight()
            .background(mainBackground)
            .onSizeChanged { size ->
                maxWidth = size.width
            }) {
        Box(
            modifier = Modifier
                .wrapContentHeight()
                .align(Alignment.BottomStart)
        ) {
            LazyColumn(
                state = lazyListState,
                modifier = Modifier.fillMaxWidth().wrapContentHeight()
                    .align(Alignment.BottomStart)

            ) {
                item {
                    Spacer(modifier = Modifier.height(50.dp))
                    TypeArea(
                        modifier = Modifier.snapshot(
                            pictureRecorder,
                            saveDir = snapshotDir,
                            isNeedMake = viewModel.makeSnapshot == SnapshotState.TODO,
                            onSuccess = {
                                viewModel.makeSnapshot(SnapshotState.Done(it))
                            }
                        ), viewModel, lazyListState, maxWidth, bottomHeight,
                        onTextChange = { text ->
                            viewModel.updateText(text)
                            editText = text
                        },
                        onKeyType = { char ->
                            typedChar = char
                        })
                    Spacer(modifier = Modifier.height(50.dp))
                }
            }
        }
        Bottom(bottomHeight, viewModel.showStatusUI(), editText, typedChar) {
            viewModel.makeSnapshot(SnapshotState.TODO)
        }
    }
}


@Composable
fun BoxScope.TypeArea(
    modifier: Modifier,
    viewModel: MainViewModel,
    scrollableState: LazyListState,
    maxWidth: Int,
    bottomHeight: Int,
    onTextChange: (String) -> Unit,
    onKeyType: (Char) -> Unit
) {
    val paperRate = 0.65     // 纸张的比例
    val textPadding = 40    // 文字边缘 padding
    val userData by viewModel.userData.collectAsState()
    val fontSize = userData.fontSize
    var lineCount by remember { mutableStateOf(1) }
    var lastLineWH by remember { mutableStateOf(Pair<Float, Float>(0f, 0f)) }       // 上一次编辑时候的行宽高
    var textLayoutResult by remember { mutableStateOf<TextLayoutResult?>(null) }

    var typeTextValue by remember { mutableStateOf(TextFieldValue("")) }
    val focusRequester = remember { FocusRequester() }

    // 自动请求焦点
    LaunchedEffect(Unit) {
        delay(100)
        focusRequester.requestFocus()
    }
    //  selection[start,end] 光标所在的选择开始的位置, e.g [0,7]->从第1个字符选择到第8个字符 [7,0]->从第8个字符选择到第1个字符
    val cursorPosition = typeTextValue.selection.end

    // 计算当前光标（行）
    val currentLine = if (textLayoutResult != null && cursorPosition != -1) {
        textLayoutResult!!.getLineForOffset(cursorPosition)
    } else {
        -1
    }
    // 计算光标所在位置的横向内容宽度和列高度
    val (lineWidth, lineHeight) = textLayoutResult?.let { layout ->
        val contentLength = layout.multiParagraph.intrinsics.annotatedString.length
        if (cursorPosition > contentLength || contentLength < 0) {
            lastLineWH
        } else {
            // 根据光标位置算出来的光标所在的行
            val horizontal = layout.getHorizontalPosition(cursorPosition, true)
            lastLineWH = Pair(
                horizontal,
                layout.getLineBottom(currentLine) - layout.getLineTop(currentLine)
            )
            lastLineWH
        }
    } ?: Pair(0f, 0f)

    // 移动的距离为：最大长度的一半 减 文本所占据的宽度 减 两个文本输入 padding 减 一个字符的大小的一半（使光标在中间）
    val offsetX by animateDpAsState(
        targetValue = ((maxWidth / 2 - lineWidth - textPadding * 2 - fontSize / 2)).pxToDp(),
        label = "paper move animation.",
    )
    val offsetY by animateDpAsState(
        targetValue = ((lineHeight * currentLine) / 2).pxToDp(),
        label = "paper move animation.",
    )
    LaunchedEffect(typeTextValue) {
        scrollableState.animateScrollToItem(0, scrollOffset = Int.MAX_VALUE)
    }
    Box(
        modifier = Modifier
            .width((((maxWidth * paperRate).toInt()).pxToDp()))
            .align(Alignment.BottomStart)
            .offset(x = offsetX)
    ) {
        Surface(
            modifier = modifier.fillMaxWidth(),
            shadowElevation = 10.dp
        ) {
            BasicTextField(
                value = typeTextValue,
                onValueChange = {
                    // 光标移动也会触发 onValueChange，这里标记文字的差异
                    if (typeTextValue.text != it.text) {
                        viewModel.playAudio(AudioType.CLICK)
                        onTextChange(it.text)
                        onKeyType(it.text.lastOrNull() ?: ' ')
                    }
                    // 保持光标始终在最后的位置（也就是无法点击文本进行光标位置移动）
                    typeTextValue = it.copy(
                        selection = TextRange(it.text.length)
                    )
                },
                modifier = Modifier
                    .height((lineCount * lineHeight).pxToDp() + bottomHeight.dp)
                    .background(Color.White)
                    .padding(textPadding.dp)
                    .focusRequester(focusRequester),
                textStyle = TextStyle.Default.copy(
                    fontSize = fontSize.sp,
                    fontFamily = viewModel.typeFontFamily(),
                    textAlign = viewModel.textAlign()
                ),
                onTextLayout = { layoutResult ->
                    textLayoutResult = layoutResult
                    val newLineCount = layoutResult.lineCount
                    if (newLineCount != lineCount) {
                        lineCount = newLineCount

                        viewModel.playAudio(AudioType.NEWLINE)
                    }
                    layoutResult.multiParagraph.intrinsics.placeholders
                },
                cursorBrush = SolidColor(Color.Transparent)
            )
        }
    }
}

@Composable
fun BoxScope.Bottom(bottomHeight: Int, showStats: Boolean, editText: String, typedChar: Char, onSnapshot: () -> Unit) {
    Column(
        modifier = Modifier
            .align(Alignment.BottomCenter)
    ) {
        Spacer(
            modifier = Modifier
                .width(1.dp)
                .background(Color(0, 0, 0, 100))
                .height(30.dp)
                .align(Alignment.CenterHorizontally)
        )
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 100.dp)
                .clip(RoundedCornerShape(topEnd = 10.dp, topStart = 10.dp))
                .height(bottomHeight.dp)
                .background(Color(216, 176, 162, 200))
        ) {
            if (showStats) {
                Row(
                    modifier = Modifier
                        .height(55.dp)
                        .padding(10.dp)
                        .clip(RoundedCornerShape(6.dp))
                        .align(Alignment.BottomEnd)
                        .background(Color(251, 247, 245))
                ) {
                    Text(
                        modifier = Modifier
                            .padding(10.dp)
                            .align(Alignment.CenterVertically),
                        text = "Typed: ${if (typedChar == '\n') "\\n" else typedChar.uppercaseChar()}",
                        style = TextStyle(fontSize = 12.sp)
                    )
                    Text(
                        modifier = Modifier
                            .padding(10.dp)
                            .align(Alignment.CenterVertically),
                        text = "Characters:${editText.length}",
                        style = TextStyle(fontSize = 12.sp)
                    )
                }
            }
        }
    }
}
