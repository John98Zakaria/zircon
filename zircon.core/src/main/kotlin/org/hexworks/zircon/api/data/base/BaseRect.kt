package org.hexworks.zircon.api.data.base

import org.hexworks.zircon.api.behavior.Boundable
import org.hexworks.zircon.api.data.Position
import org.hexworks.zircon.api.data.Rect
import org.hexworks.zircon.api.data.Size

/**
 * Base class for [Rect] implementations.
 */
abstract class BaseRect : Rect {

    override val x
        get() = position.x

    override val y
        get() = position.y

    override val width
        get() = size.width

    override val height
        get() = size.height

    override operator fun component1() = x

    override operator fun component2() = y

    override operator fun component3() = width

    override operator fun component4() = height

    override operator fun plus(rect: Rect) = Rect.create(
            position = position + rect.position,
            size = size + rect.size)

    override operator fun minus(rect: Rect) = Rect.create(
            position = position - rect.position,
            size = size - rect.size)

    override fun intersects(boundable: Boundable): Boolean {
        val otherBounds = boundable.rect
        var tw = size.width
        var th = size.height
        var rw = otherBounds.width
        var rh = otherBounds.height
        if (rw <= 0 || rh <= 0 || tw <= 0 || th <= 0) {
            return false
        }
        val tx = this.x
        val ty = this.y
        val rx = otherBounds.x
        val ry = otherBounds.y
        rw += rx
        rh += ry
        tw += tx
        th += ty
        return (rw < rx || rw > tx) &&
                (rh < ry || rh > ty) &&
                (tw < tx || tw > rx) &&
                (th < ty || th > ry)
    }

    override fun containsPosition(position: Position): Boolean {
        val (otherX, otherY) = position
        var width = width
        var height = height
        if (width or height < 0) {
            return false
        }
        val x = x
        val y = y
        if (otherX < x || otherY < y) {
            return false
        }
        width += x
        height += y
        return (width < x || width > otherX) && (height < y || height > otherY)
    }

    override fun containsBoundable(boundable: Boundable): Boolean {
        var (otherX, otherY, otherWidth, otherHeight) = boundable.rect
        var w = width
        var h = height
        val x = x
        val y = y
        if (w or h or otherWidth or otherHeight < 0) {
            return false
        }
        if (otherX < x || otherY < y) {
            return false
        }
        w += x
        otherWidth += otherX
        if (otherWidth <= otherX) {
            if (w >= x || otherWidth > w) return false
        } else {
            if (w in x..(otherWidth - 1)) return false
        }
        h += y
        otherHeight += otherY
        if (otherHeight <= otherY) {
            if (h >= y || otherHeight > h) return false
        } else {
            if (h in y..(otherHeight - 1)) return false
        }
        return true
    }

    override fun withPosition(position: Position) = Rect.create(position, size)

    override fun withSize(size: Size) = Rect.create(position, size)

    override fun withRelativePosition(position: Position) = Rect.create(this.position + position, size)

    override fun withRelativeSize(size: Size) = Rect.create(position, this.size + size)
}
