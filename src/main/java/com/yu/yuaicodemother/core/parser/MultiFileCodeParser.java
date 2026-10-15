package com.yu.yuaicodemother.core.parser;

import com.yu.yuaicodemother.ai.model.MultiFileCodeResult;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 多文件代码解析器（HTML + CSS + JS）
 *
 * @author yu🐟
 */
public class MultiFileCodeParser implements CodeParser<MultiFileCodeResult> {

    private static final Pattern HTML_CODE_PATTERN = Pattern.compile("```html\\s*\\n([\\s\\S]*?)```", Pattern.CASE_INSENSITIVE);
    private static final Pattern CSS_CODE_PATTERN = Pattern.compile("```css\\s*\\n([\\s\\S]*?)```", Pattern.CASE_INSENSITIVE);
    private static final Pattern JS_CODE_PATTERN = Pattern.compile("```(?:js|javascript)\\s*\\n([\\s\\S]*?)```", Pattern.CASE_INSENSITIVE);

    /** 起始围栏兜底：输出被 max-tokens 截断时闭合 ``` 会缺失 */
    private static final Pattern HTML_OPEN_FENCE_PATTERN = Pattern.compile("```html\\s*\\n", Pattern.CASE_INSENSITIVE);
    private static final Pattern CSS_OPEN_FENCE_PATTERN = Pattern.compile("```css\\s*\\n", Pattern.CASE_INSENSITIVE);
    private static final Pattern JS_OPEN_FENCE_PATTERN = Pattern.compile("```(?:js|javascript)\\s*\\n", Pattern.CASE_INSENSITIVE);

    @Override
    public MultiFileCodeResult parseCode(String codeContent) {
        MultiFileCodeResult result = new MultiFileCodeResult();
        // 提取各类代码
        String htmlCode = extractCodeByPattern(codeContent, HTML_CODE_PATTERN, HTML_OPEN_FENCE_PATTERN);
        String cssCode = extractCodeByPattern(codeContent, CSS_CODE_PATTERN, CSS_OPEN_FENCE_PATTERN);
        String jsCode = extractCodeByPattern(codeContent, JS_CODE_PATTERN, JS_OPEN_FENCE_PATTERN);
        // 设置HTML代码
        if (htmlCode != null && !htmlCode.trim().isEmpty()) {
            result.setHtmlCode(htmlCode.trim());
        }
        // 设置CSS代码
        if (cssCode != null && !cssCode.trim().isEmpty()) {
            result.setCssCode(cssCode.trim());
        }
        // 设置JS代码
        if (jsCode != null && !jsCode.trim().isEmpty()) {
            result.setJsCode(jsCode.trim());
        }
        return result;
    }



    /**
     * 根据正则模式提取代码，闭合围栏匹配失败时用起始围栏兜底（应对输出被截断的情况）
     *
     * @param content     原始内容
     * @param pattern     完整代码块正则
     * @param openPattern 起始围栏正则
     * @return 提取的代码
     */
    private String extractCodeByPattern(String content, Pattern pattern, Pattern openPattern) {
        Matcher matcher = pattern.matcher(content);
        if (matcher.find()) {
            return matcher.group(1);
        }
        Matcher openMatcher = openPattern.matcher(content);
        if (openMatcher.find()) {
            return content.substring(openMatcher.end());
        }
        return null;
    }
}
