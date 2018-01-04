package com.nodes.folding

/**
 * Created by Vladimir Ostaci on 28/11/2017.
 */

import com.intellij.codeInsight.daemon.RelatedItemLineMarkerInfo
import com.intellij.codeInsight.daemon.RelatedItemLineMarkerProvider
import com.intellij.codeInsight.navigation.NavigationGutterIconBuilder
import com.intellij.openapi.util.IconLoader
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiReferenceExpression
import java.util.*

class SimpleLineMarkerProvider : RelatedItemLineMarkerProvider() {

    override fun collectNavigationMarkers(reference: PsiElement, result: MutableCollection<in RelatedItemLineMarkerInfo<PsiElement>>) {
        if (reference is PsiReferenceExpression) {
            if (isValidNstackTranslationClassReference(reference)) {
                val properties = ArrayList<PsiElement>()
                properties.add(reference)
                if (properties.size > 0) {
                    val hardcodedIcon = IconLoader.getIcon("/ic_title_white_18pt.png")
                    val builder = NavigationGutterIconBuilder.create(hardcodedIcon).setTargets(properties).setTooltipText("Navigate to a simple property")
                    result.add(builder.createLineMarkerInfo(reference))
                }
            } else if (isValidAndroidStringResourceReference(reference)) {
                val properties = ArrayList<PsiElement>()
                properties.add(reference)
                if (properties.size > 0) {
                    val hardcodedIcon = IconLoader.getIcon("/ic_priority_high_white_18pt.png")
                    val builder = NavigationGutterIconBuilder.create(hardcodedIcon).setTargets(properties).setTooltipText("Navigate to a simple property")
                    result.add(builder.createLineMarkerInfo(reference))
                }
            }
        }
    }

    /**
     * Returns true if referenceExpression has this structure: Translation.section.stringName
     * */
    private fun isValidNstackTranslationClassReference(referenceExpression: PsiReferenceExpression?): Boolean {
        // A valid reference to a field inside Translation class contains 4 children: Translation.section + . + PsiReferenceParameterList + stringName
        if (referenceExpression == null || referenceExpression.children.size != 4) return false
        val nstackSectionReference = referenceExpression.firstChild    // returns 'Translation.section' element
        val translationChild = nstackSectionReference.firstChild    // returns 'Translation' element
        return translationChild.text == "Translation"
    }

    /**
     * Returns true if referenceExpression has this structure: R.string.stringName
     * */
    private fun isValidAndroidStringResourceReference(referenceExpression: PsiReferenceExpression?): Boolean {
        // A valid reference to a field inside Translation class contains 4 children: R.string + . + PsiReferenceParameterList + stringName
        if (referenceExpression == null || referenceExpression.children.size != 4) return false
        val stringSectionReference = referenceExpression.firstChild    // returns 'R.string' element
        return stringSectionReference.text == "R.string"
    }
}
