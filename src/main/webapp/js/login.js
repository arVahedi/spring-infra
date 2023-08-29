$(document).ready(function () {
    $("#LoginBtn").click(function () {
        let username = $("#Username").val();
        let password = $("#Password").val();

        let data = "{\"username\": \"" + username + "\", \"password\": \"" + password + "\"}";

        $.ajax({
            type: "POST",
            url: "./api/v1/authenticate",
            data: data,
            contentType: "application/json; charset=utf-8",
            dataType: "json",
            success: function (data) {
                console.log(data);
                // window.localStorage.setItem("authorization", data.result.token);
                location.reload();
            },
            error: function (errMsg) {
                console.log(errMsg);
            }
        });
    });
});
