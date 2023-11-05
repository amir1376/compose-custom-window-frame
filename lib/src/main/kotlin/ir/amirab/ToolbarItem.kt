package ir.amirab

import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.positionInWindow
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Density
import androidx.compose.ui.window.FrameWindowScope
import ir.amirab.util.CustomWindowDecorationAccessing
import java.awt.Rectangle
import java.awt.Shape
import java.awt.Window

object HitSpots {
    const val NO_HIT_SPOT = 0
    const val OTHER_HIT_SPOT = 1
    const val MINIMIZE_BUTTON = 2
    const val MAXIMIZE_BUTTON = 3
    const val CLOSE_BUTTON = 4
    const val MENU_BAR = 5
    const val DRAGGABLE_AREA = 6
}


private val LocalWindowHitSpots = compositionLocalOf<MutableMap<Any, Pair<Rectangle, Int>>> { error("LocalWindowHitSpots not provided") }

context (FrameWindowScope)
@Composable
fun ProvideWindowSpotContainer(
    content: @Composable () -> Unit,
) {
    val stateMap = remember {
        mutableStateMapOf<Any, Pair<Rectangle, Int>>()
    }
    var height by remember {
        mutableStateOf(0)
    }
    LaunchedEffect(
        stateMap.toMap(),
        height,
        window
    ) {
        if (CustomWindowDecorationAccessing.isSupported) {
            reDrawFrame(window, stateMap, height)
        }
    }
    CompositionLocalProvider(
        LocalWindowHitSpots provides stateMap
    ) {
        val density = LocalDensity.current
        Box(Modifier.onGloballyPositioned {
            height = with(density) {
                it.size.height.toAwtUnitSize()
            }
        }) {
            content()
        }
    }
}

context (FrameWindowScope)
@Composable
fun Modifier.windowFrameItem(
    key: Any,
    spot: Int,
) = composed {
    var shape by remember(key) {
        mutableStateOf(null as Rectangle?)
    }
    val localWindowSpots = LocalWindowHitSpots.current
    DisposableEffect(shape, key) {
        shape.let { rectangle ->
            if (rectangle != null) {
                val s = rectangle
                localWindowSpots[key] = s to spot
                onDispose {
                    localWindowSpots.remove(key)
                }
            } else {
                onDispose {}
            }
        }
    }
    onPositionInRect { shape = it }
}

context (FrameWindowScope)
@Composable
private fun Modifier.onPositionInRect(
    onChange: (Rectangle) -> Unit,
) = composed {
    val density = LocalDensity.current
    onGloballyPositioned {
        onChange(
            it.positionInWindow().toDpRectangle(
                width = it.size.width,
                height = it.size.height,
                density = density
            )
        )
    }
}


private fun Offset.toDpRectangle(
    width: Int,
    height: Int,
    density: Density,
): Rectangle {
    val offset = this
    density.run {
//      TODO investigate
//       why I have to add this margin to offset.x to accurate
//       the position of buttons in window frame.
//       it must not be!
        val weirdMargin = 11f
        return Rectangle(
            (offset.x + weirdMargin).toAwtUnitSize(),
            offset.y.toAwtUnitSize(),
            width.toAwtUnitSize(),
            height.toAwtUnitSize(),
        )
    }
}

context (Density)
private fun Int.toAwtUnitSize() = toDp().value.toInt()

context (Density)
private fun Float.toAwtUnitSize() = toDp().value.toInt()


private fun reDrawFrame(
    window: Window,
    list: Map<Any, Pair<Rectangle, Int>>,
    height: Int,
) {
    CustomWindowDecorationAccessing.setCustomDecorationEnabled(window, true)
    CustomWindowDecorationAccessing.setCustomDecorationTitleBarHeight(
        window,
        height,
    )
    val spots: Map<Shape, Int> = list.values.associate { it }
    CustomWindowDecorationAccessing.setCustomDecorationHitTestSpotsMethod(window, spots)
}
