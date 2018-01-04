package com.nodes.folding

import com.intellij.lang.ASTNode
import com.intellij.lang.folding.FoldingBuilderEx
import com.intellij.lang.folding.FoldingDescriptor
import com.intellij.openapi.editor.Document
import com.intellij.psi.PsiElement
import com.intellij.psi.util.PsiTreeUtil
import org.jetbrains.kotlin.psi.KtDotQualifiedExpression
import org.jetbrains.kotlin.psi.KtFile

/**
 * Created by Vladimir Ostaci on 21/11/2017.
 */

class KotlinTranslationFoldingBuilder : FoldingBuilderEx() {

    override fun buildFoldRegions(root: PsiElement, document: Document, quick: Boolean): Array<FoldingDescriptor> {
        if (root !is KtFile) {
            return FoldingDescriptor.EMPTY
        }
        val descriptors = ArrayList<FoldingDescriptor>()
        // Get all the reference expressions in this Java file
        val referenceExpressions = PsiTreeUtil.findChildrenOfType(root, KtDotQualifiedExpression ::class.java)
        for (referenceExpression in referenceExpressions) {
            if (isValidNstackTranslationClassReference(referenceExpression)) {
                val referencedFieldElement = referenceExpression.text
//                val referencedFieldElement = referenceExpression.resolve()
//                if (referencedFieldElement != null) {
                    val node = referenceExpression.node
                    val descriptor = FoldingDescriptor(node, node.textRange, null)
                    descriptors.add(descriptor)
//                }
            }
        }
         return descriptors.toTypedArray()
    }

    private fun isValidNstackTranslationClassReference(referenceExpression: KtDotQualifiedExpression ?): Boolean {
        // A valid reference to a field inside Translation class contains 4 children: Translation.section + . + PsiReferenceParameterList + stringName
        if (referenceExpression == null || referenceExpression.children.size != 2) return false
        val nstackSectionReference = referenceExpression.firstChild    // returns 'Translation.section' element
        val translationChild = nstackSectionReference.firstChild    // returns 'Translation' element
        return translationChild.text == "Translation"
    }

    override fun getPlaceholderText(node: ASTNode): String? {
//        val referenceName = (node.psi.reference as KtSimpleNameExpressionImpl).lastChild.text
//        val referencedFieldElement = node.psi.reference!!.resolve()
//        var value: String? = null
//        if (referencedFieldElement != null) {
//            val elements = referencedFieldElement.children
//            for (psiElement in elements) {
//                if (psiElement is PsiLiteralExpression) {
//                    value = psiElement.value as String?
//                    if (value?.length!! > MAX_FOLDED_PLACEHOLDER_TEXT_LENGTH) {
//                        value = value.substring(0, MAX_FOLDED_PLACEHOLDER_TEXT_LENGTH) + "..."
//                    }
//                    break
//                }
//            }
//        }
//        return if (value != null) value else ": ..."
        return "..."
    }

    override fun isCollapsedByDefault(node: ASTNode): Boolean {
        return true
    }

    companion object {
        val MAX_FOLDED_PLACEHOLDER_TEXT_LENGTH = 35
    }
}
