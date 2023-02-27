package ru.geekbrains.mymaterialproject.behaviors

import android.content.Context
import android.util.AttributeSet
import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.coordinatorlayout.widget.CoordinatorLayout
import ru.geekbrains.mymaterialproject.R

class MyBottomSheetBehavior(context: Context?, attrs: AttributeSet? = null) :
    CoordinatorLayout.Behavior<ConstraintLayout>(context, attrs) {

    override fun layoutDependsOn(
        parent: CoordinatorLayout,
        child: ConstraintLayout,
        dependency: View
    ): Boolean {
        return (dependency.id == R.id.main)
    }

    override fun onDependentViewChanged(
        parent: CoordinatorLayout,
        child: ConstraintLayout,
        dependency: View
    ): Boolean {
        if (dependency.id == R.id.main){

        }
        return true
    }
}