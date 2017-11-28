package com.nodes.folding

import com.intellij.lang.ASTNode
import com.intellij.lang.folding.FoldingBuilderEx
import com.intellij.lang.folding.FoldingDescriptor
import com.intellij.openapi.editor.Document
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiJavaFile
import com.intellij.psi.PsiLiteralExpression
import com.intellij.psi.PsiReferenceExpression
import com.intellij.psi.impl.source.tree.java.PsiReferenceExpressionImpl
import com.intellij.psi.util.PsiTreeUtil
import java.util.*

/**
 * Created by Vladimir Ostaci on 21/11/2017.
 */

class TranslationFoldingBuilder : FoldingBuilderEx() {

    override fun buildFoldRegions(root: PsiElement, document: Document, quick: Boolean): Array<FoldingDescriptor> {
//        if (!(root is PsiJavaFile || root is org.jetbrains.kotlin.psi.KtFile)) {
        if (root !is PsiJavaFile) {
            return FoldingDescriptor.EMPTY
        }

        val descriptors = ArrayList<FoldingDescriptor>()
        // Get all the reference expressions in this Java file
        val referenceExpressions = PsiTreeUtil.findChildrenOfType(root, PsiReferenceExpression::class.java)
        for (referenceExpression in referenceExpressions) {
            if (isValidNstackTranslationClassReference(referenceExpression)) {
                val referencedFieldElement = referenceExpression.resolve()
                if (referencedFieldElement != null) {
                    val node = referenceExpression.node
                    val descriptor = FoldingDescriptor(node, node.textRange, null)
                    descriptors.add(descriptor)
                }
            }
        }
        return descriptors.toTypedArray()
    }

    private fun isValidNstackTranslationClassReference(referenceExpression: PsiReferenceExpression?): Boolean {
        // A valid reference to a field inside Translation class contains 4 children: Translation.section + . + PsiReferenceParameterList + stringName
        if (referenceExpression == null || referenceExpression.children.size != 4) return false
        val nstackSectionReference = referenceExpression.firstChild    // returns 'Translation.section' element
        val translationChild = nstackSectionReference.firstChild    // returns 'Translation' element
        return translationChild.text == "Translation"
    }

    override fun getPlaceholderText(node: ASTNode): String? {
        val referenceName = (node.psi.reference as PsiReferenceExpressionImpl).lastChild.text
        val referencedFieldElement = node.psi.reference!!.resolve()
        var value: String? = null
        if (referencedFieldElement != null) {
            val elements = referencedFieldElement.children
            for (psiElement in elements) {
                if (psiElement is PsiLiteralExpression) {
                    value = psiElement.value as String?
                    if (value?.length!! > MAX_FOLDED_PLACEHOLDER_TEXT_LENGTH) {
                        value = value.substring(0, MAX_FOLDED_PLACEHOLDER_TEXT_LENGTH) + "..."
                    }
                    break
                }
            }
        }
        return if (value != null) value else ": ..."
    }

    override fun isCollapsedByDefault(node: ASTNode): Boolean {
        return true
    }

    companion object {
        val MAX_FOLDED_PLACEHOLDER_TEXT_LENGTH = 35
    }
}
