<#import "/main/macros.ftl" as macros>
<#assign importCss=["/css/friendShip/fsMain.css"]>
<#assign importJs=["/js/friendShip/fsMain.js"]>
<@macros.navhead importJs=importJs importCss=importCss titleName="我的好友"></@macros.navhead>

<div class="main-contain" style="margin-top: 1rem">
    <div class="row friend-div">
        <div class="friendList col-3">

        </div>

        <div class="content-div col-9">
            <div class="content-top">

            </div>
            <div class="content-bottom row">
                <div class="col-10">
                    <input class="in-msg form-control">
                </div>
                <div class="col-2 pic-div">
                    <input type="file" class="file" id="picFile" hidden>
                    <svg t="1585475072130" id="pic-send-icon" class="icon" viewBox="0 0 1024 1024" version="1.1" xmlns="http://www.w3.org/2000/svg" p-id="5283" width="200" height="200"><path d="M840.5 798.2L662.3 599.5l-151 173.7-173.7-173.7-167.7 201c-21 30.4 0.9 71.8 37.9 71.6l594.7-3.3c36.2-0.1 57.8-40.3 38-70.6z" fill="#FFB89A" p-id="5284"></path><path d="M741.6 647.3l-52.3-47.7c-12.2-11.2-31.2-10.3-42.4 1.9s-10.3 31.2 1.9 42.4l52.3 47.7c5.8 5.3 13 7.8 20.2 7.8 8.1 0 16.2-3.3 22.2-9.8 11.2-12.1 10.3-31.1-1.9-42.3zM631.2 546.5c-12.4-11-31.4-9.8-42.3 2.6l-98.8 111.7-171-165.7L87.9 724.7c-11.8 11.7-11.8 30.7-0.1 42.4 5.9 5.9 13.6 8.9 21.3 8.9 7.6 0 15.3-2.9 21.1-8.7l189.4-188.1 173.8 168.5L633.8 589c11-12.5 9.8-31.5-2.6-42.5z" fill="#33CC99" p-id="5285"></path><path d="M721.3 342.8m-35.1 0a35.1 35.1 0 1 0 70.2 0 35.1 35.1 0 1 0-70.2 0Z" fill="#33CC99" p-id="5286"></path><path d="M743.2 175.1H191.6c-70.6 0-128.3 57.7-128.3 128.3v499.2c0 70.6 57.7 128.3 128.3 128.3h551.5c70.6 0 128.3-57.7 128.3-128.3V303.5c0.1-70.6-57.7-128.4-128.2-128.4z m68.3 627.6c0 18.1-7.1 35.2-20.1 48.2-13 13-30.1 20.1-48.2 20.1H191.6c-18.1 0-35.2-7.1-48.2-20.1-13-13-20.1-30.1-20.1-48.2V303.5c0-18.1 7.1-35.2 20.1-48.2 13-13 30.1-20.1 48.2-20.1h551.5c18.1 0 35.2 7.1 48.2 20.1 13 13 20.1 30.1 20.1 48.2v499.2z" fill="#45484C" p-id="5287"></path><path d="M799.7 90.9H237.2c-16.6 0-30 13.4-30 30s13.4 30 30 30h562.4c26.1 0 50.8 10.3 69.4 28.9 18.6 18.6 28.9 43.3 28.9 69.4v482.4c0 16.6 13.4 30 30 30s30-13.4 30-30V249.2C958 161.9 887 90.9 799.7 90.9z" fill="#45484C" p-id="5288"></path></svg>
                    <svg t="1586360464685" id="send-icon" class="icon sendBtn" viewBox="0 0 1024 1024" version="1.1" xmlns="http://www.w3.org/2000/svg" p-id="4063" width="200" height="200"><path d="M931.392 11.264L45.12 530.688c-28.736 16.896-43.52 39.424-45.12 61.248v8.128c2.048 26.112 23.04 49.984 61.632 60.416l171.968 46.592a34.304 34.304 0 0 0 41.28-25.536 35.584 35.584 0 0 0-23.808-43.136L79.68 592l873.408-511.872-95.232 703.488c-1.408 10.432-9.152 15.68-18.752 12.992l-365.632-100.288 296.32-305.856a36.416 36.416 0 0 0 0-50.24 33.728 33.728 0 0 0-48.704 0l-324.8 335.36a110.72 110.72 0 0 0-7.872 9.088 35.52 35.52 0 0 0-16.128 30.784 104 104 0 0 0-5.248 32.64v206.4c0 49.664 53.568 79.168 93.568 51.712l166.272-114.368c10.24-6.976 16-19.136 15.232-31.872a35.712 35.712 0 0 0-19.2-29.504 33.28 33.28 0 0 0-34.24 2.304L435.84 937.856v-178.432l385.472 105.6c49.6 13.632 97.472-19.072 104.576-71.808l97.152-717.568c8.448-60.48-40-94.72-91.648-64.384z" fill="#515151" p-id="4064"></path></svg>
                </div>
                <#--<div class="col-1 sned-div">-->
                    <#--<svg t="1586360464685" id="send-icon" class="icon sendBtn" viewBox="0 0 1024 1024" version="1.1" xmlns="http://www.w3.org/2000/svg" p-id="4063" width="200" height="200"><path d="M931.392 11.264L45.12 530.688c-28.736 16.896-43.52 39.424-45.12 61.248v8.128c2.048 26.112 23.04 49.984 61.632 60.416l171.968 46.592a34.304 34.304 0 0 0 41.28-25.536 35.584 35.584 0 0 0-23.808-43.136L79.68 592l873.408-511.872-95.232 703.488c-1.408 10.432-9.152 15.68-18.752 12.992l-365.632-100.288 296.32-305.856a36.416 36.416 0 0 0 0-50.24 33.728 33.728 0 0 0-48.704 0l-324.8 335.36a110.72 110.72 0 0 0-7.872 9.088 35.52 35.52 0 0 0-16.128 30.784 104 104 0 0 0-5.248 32.64v206.4c0 49.664 53.568 79.168 93.568 51.712l166.272-114.368c10.24-6.976 16-19.136 15.232-31.872a35.712 35.712 0 0 0-19.2-29.504 33.28 33.28 0 0 0-34.24 2.304L435.84 937.856v-178.432l385.472 105.6c49.6 13.632 97.472-19.072 104.576-71.808l97.152-717.568c8.448-60.48-40-94.72-91.648-64.384z" fill="#515151" p-id="4064"></path></svg>-->
                <#--</div>-->
            </div>
        </div>
    </div>

</div>

