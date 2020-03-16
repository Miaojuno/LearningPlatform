<#import "/main/macros.ftl" as macros>
<#assign importCss=[]>
<#assign importJs=["/webjars/visjs/4.21.0/vis.js","/js/question/pointShow.js"]>
<@macros.navhead importJs=importJs importCss=importCss titleName="知识点"></@macros.navhead>

<div class="main-contain" style="margin-top: 1rem;position: relative;">
    <div id="network_id" class="network"></div><!-- 拓扑图容器-->
    <div id="pointDetail"
         style="position: absolute;right: 0.5rem;top: 0.5rem;border-radius: 0.2rem;box-shadow: 2px 2px 8px #333333;padding: 0.5rem;">
        <div class="textShow">
            <p>知识点详情:</p>
            <p hidden class="nodeId"></p>
            <p class="pointDetail">内容:</p>
            <p class="chapter">章节:</p>
            <p class="isbn">ISBN:</p>
            <p class="grade">年级:</p>
            <p class="distribution">考试要求分布:</p>
            <p class="frequency">频次:</p>
        </div>
        <div class="btnDiv">
            <button class="btn btn-primary jumpBtn" style="padding: 0.05rem 0.4rem;">练习该知识点</button>
        </div>
    </div>
</div>


