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
                <div class="col-1 pic-div">
                    <input type="file" class="file" id="picFile" hidden>
                    <svg t="1585475072130" id="pic-send-icon" class="icon" viewBox="0 0 1024 1024" version="1.1" xmlns="http://www.w3.org/2000/svg" p-id="5283" width="200" height="200"><path d="M840.5 798.2L662.3 599.5l-151 173.7-173.7-173.7-167.7 201c-21 30.4 0.9 71.8 37.9 71.6l594.7-3.3c36.2-0.1 57.8-40.3 38-70.6z" fill="#FFB89A" p-id="5284"></path><path d="M741.6 647.3l-52.3-47.7c-12.2-11.2-31.2-10.3-42.4 1.9s-10.3 31.2 1.9 42.4l52.3 47.7c5.8 5.3 13 7.8 20.2 7.8 8.1 0 16.2-3.3 22.2-9.8 11.2-12.1 10.3-31.1-1.9-42.3zM631.2 546.5c-12.4-11-31.4-9.8-42.3 2.6l-98.8 111.7-171-165.7L87.9 724.7c-11.8 11.7-11.8 30.7-0.1 42.4 5.9 5.9 13.6 8.9 21.3 8.9 7.6 0 15.3-2.9 21.1-8.7l189.4-188.1 173.8 168.5L633.8 589c11-12.5 9.8-31.5-2.6-42.5z" fill="#33CC99" p-id="5285"></path><path d="M721.3 342.8m-35.1 0a35.1 35.1 0 1 0 70.2 0 35.1 35.1 0 1 0-70.2 0Z" fill="#33CC99" p-id="5286"></path><path d="M743.2 175.1H191.6c-70.6 0-128.3 57.7-128.3 128.3v499.2c0 70.6 57.7 128.3 128.3 128.3h551.5c70.6 0 128.3-57.7 128.3-128.3V303.5c0.1-70.6-57.7-128.4-128.2-128.4z m68.3 627.6c0 18.1-7.1 35.2-20.1 48.2-13 13-30.1 20.1-48.2 20.1H191.6c-18.1 0-35.2-7.1-48.2-20.1-13-13-20.1-30.1-20.1-48.2V303.5c0-18.1 7.1-35.2 20.1-48.2 13-13 30.1-20.1 48.2-20.1h551.5c18.1 0 35.2 7.1 48.2 20.1 13 13 20.1 30.1 20.1 48.2v499.2z" fill="#45484C" p-id="5287"></path><path d="M799.7 90.9H237.2c-16.6 0-30 13.4-30 30s13.4 30 30 30h562.4c26.1 0 50.8 10.3 69.4 28.9 18.6 18.6 28.9 43.3 28.9 69.4v482.4c0 16.6 13.4 30 30 30s30-13.4 30-30V249.2C958 161.9 887 90.9 799.7 90.9z" fill="#45484C" p-id="5288"></path></svg>
                </div>
                <div class="col-9">
                    <input class="in-msg form-control">
                </div>
                <div class="col-2">
                    <button class="sendBtn btn btn-success">发送</button>
                </div>
            </div>
        </div>
    </div>

</div>

