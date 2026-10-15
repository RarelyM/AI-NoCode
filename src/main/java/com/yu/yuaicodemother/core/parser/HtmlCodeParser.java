package com.yu.yuaicodemother.core.parser;

import com.yu.yuaicodemother.ai.model.HtmlCodeResult;
import lombok.extern.slf4j.Slf4j;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * HTML 单文件代码解析器
 *
 * @author 鱼🐟
 */
@Slf4j
public class HtmlCodeParser implements CodeParser<HtmlCodeResult> {

    private static final Pattern HTML_CODE_PATTERN = Pattern.compile("```html\\s*\\n([\\s\\S]*?)```", Pattern.CASE_INSENSITIVE);

    /** 起始围栏（无闭合围栏时的兜底匹配）：模型输出被 max-tokens 截断时闭合 ``` 会缺失 */
    private static final Pattern HTML_OPEN_FENCE_PATTERN = Pattern.compile("```html\\s*\\n", Pattern.CASE_INSENSITIVE);

    @Override
    public HtmlCodeResult parseCode(String codeContent) {
        HtmlCodeResult result = new HtmlCodeResult();
        // 提取 HTML 代码
        String htmlCode = extractHtmlCode(codeContent);
        if (htmlCode != null && !htmlCode.trim().isEmpty()) {
            result.setHtmlCode(htmlCode.trim());
        } else {
            // 如果没有找到代码块，将整个内容作为HTML
            result.setHtmlCode(codeContent.trim());
            log.warn("没有找到HTML代码块，将整个内容作为HTML");
        }
        return result;
    }

    /**
     * 提取HTML代码内容
     *
     * @param content 原始内容
     * @return HTML代码
     */
    private String extractHtmlCode(String content) {
        Matcher matcher = HTML_CODE_PATTERN.matcher(content);
        if (matcher.find()) {
            return matcher.group(1);
        }
        // 截断兜底：有起始围栏但没有闭合围栏，取起始围栏之后的全部内容
        Matcher openMatcher = HTML_OPEN_FENCE_PATTERN.matcher(content);
        if (openMatcher.find()) {
            log.warn("HTML 代码块未闭合（输出可能被 max-tokens 截断），已提取起始围栏之后的内容");
            return content.substring(openMatcher.end());
        }
        return null;
    }
}
