package ru.geekbrains.mymaterialproject.behaviors

import android.content.Context
import android.util.AttributeSet
import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.coordinatorlayout.widget.CoordinatorLayout
import ru.geekbrains.mymaterialproject.R
import java.lang.Math.abs

class PictureContainerBehavior(context: Context?, attrs: AttributeSet? = null) :
    CoordinatorLayout.Behavior<ConstraintLayout>(context, attrs) {

    override fun layoutDependsOn(
        parent: CoordinatorLayout,
        child: ConstraintLayout,
        dependency: View
    ): Boolean {
        return if(dependency.id == R.id.bottom_sheet_container) {
//            child.y = dependency.y - child.height
            true
        } else {false}
    }

    override fun onDependentViewChanged(
        parent: CoordinatorLayout,
        child: ConstraintLayout,
        dependency: View
    ): Boolean {
        if (dependency.id == R.id.bottom_sheet_container) {
            val imageViewHeight = child.getViewById(R.id.imageView).height
//            val buttonViewHeight = child.getViewById(R.id.playVideoBtn).height
            val childBottomPadding = child.getViewById(R.id.imageView).paddingBottom
            //val appbarHeignt = parent.resources.obtainAttributes()
            val childMaxScrollHeight = child.height - (imageViewHeight + childBottomPadding)
            val childY = dependency.y - child.height

            child.y = if(childY >= -(childMaxScrollHeight)) childY
            else (-(childMaxScrollHeight)).toFloat()

//            val childY = dependency.y - child.height
//            if (childY > imageViewHeight) child.y = childY
//            else child.y = imageViewHeight.toFloat()
        }
        return true
    }

}