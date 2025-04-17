package org.ytymark.test.inline;

/**
 * 项目名称：ytymark
 * 作者：渊渟岳
 * 描述：链接测试
 */
public class LinkDemo {
    public static void main(String[] args) {
        String markdownText1 = "[123]()";
        String markdownText2 = "[[]]()";
        String markdownText3 = "[123[abc]()";
        String markdownText4 = "[[[]()";
        String markdownText5 = "[123[abc]()]()";
        String markdownText6 = "[123![abc]()]()";
        String markdownText7 = "[123](abc())";

        MarkdownProcessor processor = new MarkdownProcessor();

        String html1 = processor.process(markdownText1);
        String html2 = processor.process(markdownText2);
        String html3 = processor.process(markdownText3);
        String html4 = processor.process(markdownText4);
        String html5 = processor.process(markdownText5);
        String html6 = processor.process(markdownText6);
        String html7 = processor.process(markdownText7);

        System.out.println(html1);  // 解析为：<a href="">123</a>
        System.out.println(html2);  // 解析为：<a href="">[]</a>
        System.out.println(html3);  // 解析为：123<a href="">abc</a>
        System.out.println(html4);  // 解析为：<a href=""></a>
        System.out.println(html5);  // 解析为：<a href="">123[abc]()</a>
        System.out.println(html6);  // 解析为：<a href="">123![abc]()</a>
        System.out.println(html7);  // 解析成功
    }
}
