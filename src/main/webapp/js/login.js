$(document).ready(function () {
    $("#LoginBtn").click(function () {
        let username = $("#Username").val();
        let password = $("#Password").val();

        let data = JSON.stringify({
                   username: username,
                   password: password
               });

        $.ajax({
            type: "POST",
            url: "./api/v1/authenticate",
            data: data,
            contentType: "application/json; charset=utf-8",
            dataType: "json",
            success: function (data) {
                console.log(data);
//                localStorage.setItem("authorization", data.result.token);
                sessionStorage.setItem("authorization", data.result.token);
                location.reload();
            },
            error: function (errMsg) {
                console.log(errMsg);
            }
        });
    });
});
