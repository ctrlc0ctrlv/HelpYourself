package com.easyege.examhelper;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.webkit.WebView;

public class MathActivity extends AppCompatActivity {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.math);
        final WebView w = findViewById(R.id.webview);
        w.getSettings().setJavaScriptEnabled(true);
        //w.getSettings().setBuiltInZoomControls(true);
        String Url = "</style>" + "<script type='text/x-mathjax-config'>" + " MathJax.Hub.Config({" + " showMathMenu: false," + " jax: ['input/TeX','output/HTML-CSS', 'output/CommonHTML']," + " extensions: ['tex2jax.js','MathMenu.js','MathZoom.js', 'CHTML-preview.js']," + " tex2jax: { inlineMath: [ ['$','$'] ], processEscapes: true }," + " TeX: {" + " extensions:['AMSmath.js','AMSsymbols.js'," + " 'noUndefined.js']" + " }" + " });" + "</script>" + "<script type='text/javascript' src='file:///android_asset/MathJax/MathJax.js'>" + "</script>" + "<p style=\"line-height:2; padding: 0 0\" align=\"justify\">" + "<span >";
        // Demo display equation url += "This is a display equation: $$P=\frac{F}{A}$$";
        Url += "This is also an identical display equation with different format:\\[P=\\frac{F}{A+B}\\]";
        // equations aligned at equal sign url += "You can also put aligned equations just like Latex:";
        String align = "\\begin{aligned}" + "F\\; &= P \\times (A+B) = 4000 \\times 0.2 = 800\\; \\text{N}\\end{aligned}";
        align += "Так же работает русский текст";
        align += "\\begin{aligned} A_{2} + B_{2} = C_{1} \\end{aligned}";
        align += "\\begin{aligned} \\frac{(a+b)^3}{4} - \\frac{(a-b)^2}{4} = ab \\end{aligned}";
        align += "\\begin{aligned} _{n}^{m}X + _{k}^{l}Y \\to \\dots \\end{aligned}";
        align += "\\begin{aligned} \\text{Можно делать так: }(2x + \\frac{1}{3x})^2 \\text{ или даже так: } \\left( 2x + \\frac{1}{3x} \\right)^2 \\text{ или не так, но все равно работает} \\end{aligned}";
        align += "\\begin{aligned} \\sum_{i=0}^n i^2 = \\frac{(n^2+n)(2n+1)}{6} \\end{aligned}";
        Url += align;
        // Finally, must enclose the brackets url += "</span></p>";
        w.loadDataWithBaseURL("http://bar", Url, "text/html", "utf-8", "");
    }
}