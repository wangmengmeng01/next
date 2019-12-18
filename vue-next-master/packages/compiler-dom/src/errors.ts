import {
  SourceLocation,
  CompilerError,
  createCompilerError,
  ErrorCodes
} from '@vue/compiler-core'

export interface DOMCompilerError extends CompilerError {
  code: DOMErrorCodes
}

export function createDOMCompilerError(
  code: DOMErrorCodes,
  loc?: SourceLocation
): DOMCompilerError {
  return createCompilerError(
    code,
    loc,
    __DEV__ || !__BROWSER__ ? DOMErrorMessages : undefined
  )
}

export const enum DOMErrorCodes {
  X_V_HTML_NO_EXPRESSION = ErrorCodes.__EXTEND_POINT__,
  X_V_HTML_WITH_CHILDREN,
  X_V_TEXT_NO_EXPRESSION,
  X_V_TEXT_WITH_CHILDREN
}

export const DOMErrorMessages: { [code: number]: string } = {
  [DOMErrorCodes.X_V_HTML_NO_EXPRESSION]: `v-html is missing expression.`,
  [DOMErrorCodes.X_V_HTML_WITH_CHILDREN]: `v-html will override element children.`,
  [DOMErrorCodes.X_V_TEXT_NO_EXPRESSION]: `v-text is missing expression.`,
  [DOMErrorCodes.X_V_TEXT_WITH_CHILDREN]: `v-text will override element children.`
}
