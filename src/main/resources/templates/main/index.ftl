<#import "/main/macros.ftl" as macros>
<#assign importCss=[]>
<#assign importJs=[]>
<@macros.navhead importJs=importJs importCss=importCss></@macros.navhead>

<section>
    index hello
    loginUserAccount:
    ${Session ["loginUserAccount"]}
</section>