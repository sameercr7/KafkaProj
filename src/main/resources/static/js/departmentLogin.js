// window.onload = function () {
//     var ce2 = document.getElementById('ce2');
//
//     if (ce2) {
//         document.getElementById("ce2").innerHTML = "";
//         var url = '/data/ce2';
//         var ce2NamesOptions = document.getElementById('ce2');
//         var apiKey = 'GCCLOUD123456';
//
//         // fetch(url, {
//         //     method: 'GET',
//         //     headers: {
//         //         'API-KEY': apiKey,
//         //         'Accept': 'application/json',
//         //     }
//         // })
//         fetch(url)
//             .then(response => {
//                 if (!response.ok) {
//                     throw new Error('Network response was not ok');
//                 }
//                 return response.json();
//             })
//             .then(data => {
//                 var ce2Names = data;
//                 var fix = document.createElement("option");
//                 fix.textContent = "Select CE II";
//                 fix.value = "";
//                 ce2NamesOptions.appendChild(fix);
//
//                 for (var i = 0; i < ce2Names.length; i++) {
//                     var option = ce2Names[i];
//                     var element = document.createElement("option");
//                     element.textContent = option;
//                     element.value = option;
//                     ce2NamesOptions.appendChild(element);
//                 }
//             })
//             .catch(error => console.log(error));
//     }
// };


window.onload = function () {
    var ce1 = document.getElementById('ce1');

    if (ce1) {
        document.getElementById("ce1").innerHTML = "";
        var url = '/data/ce1';
        var ce1NamesOptions = document.getElementById('ce1');
        var apiKey = 'GCCLOUD123456';

        // fetch(url, {
        //     method: 'GET',
        //     headers: {
        //         'API-KEY': apiKey,
        //         'Accept': 'application/json',
        //     }
        // })
        fetch(url)
            .then(response => {
                if (!response.ok) {
                    throw new Error('Network response was not ok');
                }
                return response.json();
            })
            .then(data => {
                var ce1Names = data;
                var fix = document.createElement("option");
                fix.textContent = "Select CE I";
                fix.value = "";
                ce1NamesOptions.appendChild(fix);

                for (var i = 0; i < ce1Names.length; i++) {
                    var option = ce1Names[i];
                    var element = document.createElement("option");
                    element.textContent = option;
                    element.value = option;
                    ce1NamesOptions.appendChild(element);
                }
            })
            .catch(error => console.log(error));
    }
};

// Js For Fetch Sub Admin List From Userspring Table

function getSubAdmin() {

    var choice = document.getElementById("subAdmin");
    var subAdmin = choice.value;
    document.getElementById("subAdmin").innerHTML = "";
    var url = '/data/subAdmin';

    fetch(url)
        .then(response => {
            if (!response.ok) {
                throw new Error('Network response was not ok');
            }
            return response.json();
        })
        .then(data => {

            var subAdminNames = data;
            var subAdminNamesOptions = document.getElementById('subAdmin');
            var fix = document.createElement("option");
            fix.textContent = "Select Sub Admin";
            fix.value = "";
            subAdminNamesOptions.appendChild(fix);

            for (var i = 0; i < subAdminNames.length; i++) {
                var option = subAdminNames[i];
                var element = document.createElement("option");
                element.textContent = option;
                element.value = option;
                subAdminNamesOptions.appendChild(element);
            }
        })
        .catch(error => console.log(error));
}


function getce2() {
    var choice = document.getElementById("ce1");
    var ce1 = choice.value;
    document.getElementById("ce2").innerHTML = "";
    var url = '/data/ce2/' + ce1;
    var apiKey = 'GCCLOUD123456';

    // fetch(url, {
    //     method: 'GET',
    //     headers: {
    //         'API-KEY': apiKey,
    //         'Accept': 'application/json',
    //     },
    // })
    fetch(url)
        .then(response => {
            if (!response.ok) {
                throw new Error('Network response was not ok');
            }
            return response.json();
        })
        .then(data => {
            var ce2Names = data;
            var ce2NamesOptions = document.getElementById('ce2');
            var fix = document.createElement("option");
            fix.textContent = "Select CE II";
            fix.value = "";
            ce2NamesOptions.appendChild(fix);

            for (var i = 0; i < ce2Names.length; i++) {
                var option = ce2Names[i];
                var element = document.createElement("option");
                element.textContent = option;
                element.value = option;
                ce2NamesOptions.appendChild(element);
            }
        })
        .catch(error => console.log(error));
}

function getse() {
    var choice = document.getElementById("ce2");
    var ce2 = choice.value;
    document.getElementById("se").innerHTML = "";
    var url = '/data/se/' + ce2;
    var apiKey = 'GCCLOUD123456';

    // fetch(url, {
    //     method: 'GET',
    //     headers: {
    //         'API-KEY': apiKey,
    //         'Accept': 'application/json',
    //     },
    // })
    fetch(url)
        .then(response => {
            if (!response.ok) {
                throw new Error('Network response was not ok');
            }
            return response.json();
        })
        .then(data => {
            var seNames = data;
            var seNamesOptions = document.getElementById('se');
            var fix = document.createElement("option");
            fix.textContent = "Select SE";
            fix.value = "";
            seNamesOptions.appendChild(fix);

            for (var i = 0; i < seNames.length; i++) {
                var option = seNames[i];
                var element = document.createElement("option");
                element.textContent = option;
                element.value = option;
                seNamesOptions.appendChild(element);
            }
        })
        .catch(error => console.log(error));
}


//For Ee
function getSeForEe() {
    var choice = document.getElementById("ce");
    var ce2 = choice.value;
    document.getElementById("se").innerHTML = "";
    var url = '/data/se/' + ce2;
    var apiKey = 'GCCLOUD123456';

    // fetch(url, {
    //     method: 'GET',
    //     headers: {
    //         'API-KEY': apiKey,
    //         'Accept': 'application/json',
    //     },
    // })
    fetch(url)
        .then(response => {
            if (!response.ok) {
                throw new Error('Network response was not ok');
            }
            return response.json();
        })
        .then(data => {
            var seNames = data;
            var seNamesOptions = document.getElementById('se');
            var fix = document.createElement("option");
            fix.textContent = "Select SE";
            fix.value = "";
            seNamesOptions.appendChild(fix);

            for (var i = 0; i < seNames.length; i++) {
                var option = seNames[i];
                var element = document.createElement("option");
                element.textContent = option;
                element.value = option;
                seNamesOptions.appendChild(element);
            }
        })
        .catch(error => console.log(error));
}

function getSe() {
    var choice = document.getElementById("ce");
    var ce2 = choice.value;
    document.getElementById("se").innerHTML = "";
    var url = '/data/se/' + ce2;
    var apiKey = 'GCCLOUD123456';

    // fetch(url, {
    //     method: 'GET',
    //     headers: {
    //         'API-KEY': apiKey,
    //         'Accept': 'application/json',
    //     },
    // })
    fetch(url)
        .then(response => {
            if (!response.ok) {
                throw new Error('Network response was not ok');
            }
            return response.json();
        })
        .then(data => {
            var seNames = data;
            var seNamesOptions = document.getElementById('se');
            var fix = document.createElement("option");
            fix.textContent = "Select SE";
            fix.value = "";
            seNamesOptions.appendChild(fix);

            for (var i = 0; i < seNames.length; i++) {
                var option = seNames[i];
                var element = document.createElement("option");
                element.textContent = option;
                element.value = option;
                seNamesOptions.appendChild(element);
            }
        })
        .catch(error => console.log(error));
}

function getee() {
    var choice = document.getElementById("se");
    var se = choice.value;
    document.getElementById("ee").innerHTML = "";
    var url = '/data/ee/' + se;
    var apiKey = 'GCCLOUD123456';

    // fetch(url, {
    //     method: 'GET',
    //     headers: {
    //         'API-KEY': apiKey,
    //         'Accept': 'application/json',
    //     },
    // })
    fetch(url)
        .then(response => {
            if (!response.ok) {
                throw new Error('Network response was not ok');
            }
            return response.json();
        })
        .then(data => {
            var eeNames = data;
            var eeNamesOptions = document.getElementById('ee');
            var fix = document.createElement("option");
            fix.textContent = "Select EE";
            fix.value = "";
            eeNamesOptions.appendChild(fix);

            for (var i = 0; i < eeNames.length; i++) {
                var option = eeNames[i];
                var element = document.createElement("option");
                element.textContent = option;
                element.value = option;
                eeNamesOptions.appendChild(element);
            }
        })
        .catch(error => console.log(error));
}


function hod() {
    var admin = document.getElementById("admin");
    var ce2 = document.getElementById("ce2");
    var se = document.getElementById("se");
    var ee = document.getElementById("ee");

    admin.name = "username";
    admin.value = "HOD";
    ce2.name = "";
    se.name = "";
    ee.name = "";

    var pwd = document.getElementById("pwd");

    if (pwd) {
        document.getElementById("pwd").style.display = "block";
    }
    document.getElementById("ce1d").style.display = "none";
    document.getElementById("ce2d").style.display = "none";
    document.getElementById("sed").style.display = "none";
    document.getElementById("eed").style.display = "none";

}

function deputyAdmin() {
    var admin = document.getElementById("admin");
    var ce2 = document.getElementById("ce2");
    var se = document.getElementById("se");
    var ee = document.getElementById("ee");

    admin.name = "username";
    admin.value = "deputyAdmin";
    ce2.name = "";
    se.name = "";
    ee.name = "";

    var pwd = document.getElementById("pwd");

    if (pwd) {
        document.getElementById("pwd").style.display = "block";
    }
    document.getElementById("ce1d").style.display = "none";
    document.getElementById("ce2d").style.display = "none";
    document.getElementById("sed").style.display = "none";
    document.getElementById("eed").style.display = "none";

}


function subAdmin() {

    var subAdmin = document.getElementById("subAdmin");
    var admin = document.getElementById("admin");
    var ce1 = document.getElementById("ce1");
    var ce2 = document.getElementById("ce2");
    var se = document.getElementById("se");
    var ee = document.getElementById("ee");

    subAdmin.name = "username";
    admin.name = "";
    ce1.name = "";
    ce2.name = "";
    se.name = "";
    ee.name = "";

    if (document.getElementById("subAdmind").style.display === "none") {

        document.getElementById("subAdmind").style.display = "block";
        var pwd = document.getElementById("pwd");
        if (pwd) {
            document.getElementById("pwd").style.display = "block";
        }

    } else {
        document.getElementById("ce1d").style.display = "none";
        document.getElementById("ce2d").style.display = "none";
        document.getElementById("sed").style.display = "none";
        document.getElementById("eed").style.display = "none";

    }
}

function admin() {
    var admin = document.getElementById("admin");
    var ce2 = document.getElementById("ce2");
    var se = document.getElementById("se");
    var ee = document.getElementById("ee");

    admin.name = "username";
    admin.value = "CHIEF WATER RESOURCE (ADMIN)";
    ce2.name = "";
    se.name = "";
    ee.name = "";

    var pwd = document.getElementById("pwd");
    if (pwd) {
        document.getElementById("pwd").style.display = "block";
    }
    document.getElementById("ce1d").style.display = "none";
    document.getElementById("ce2d").style.display = "none";
    document.getElementById("sed").style.display = "none";
    document.getElementById("eed").style.display = "none";
    document.getElementById("subAdmind").style.display = "none";

}

function showCe1() {

    var admin = document.getElementById("admin");
    var ce1 = document.getElementById("ce1");
    var ce2 = document.getElementById("ce2");
    var se = document.getElementById("se");
    var ee = document.getElementById("ee");

    admin.name = "";
    ce1.name = "username";
    ce2.name = "";
    se.name = "";
    ee.name = "";

    if (document.getElementById("ce1d").style.display === "none") {

        document.getElementById("ce1d").style.display = "block";
        var pwd = document.getElementById("pwd");
        if (pwd) {
            document.getElementById("pwd").style.display = "block";
        }

    } else {

        document.getElementById("ce2d").style.display = "none";
        document.getElementById("sed").style.display = "none";
        document.getElementById("eed").style.display = "none";
        document.getElementById("subAdmind").style.display = "none";

    }
}

function showCe2() {

    var admin = document.getElementById("admin");
    var ce1 = document.getElementById("ce1");
    var ce2 = document.getElementById("ce2");
    var se = document.getElementById("se");
    var ee = document.getElementById("ee");

    admin.name = "";
    ce1.name = "";
    ce2.name = "username";
    se.name = "";
    ee.name = "";

    if (document.getElementById("ce2d").style.display === "none") {

        document.getElementById("ce1d").style.display = "block";
        document.getElementById("ce2d").style.display = "block";
        var pwd = document.getElementById("pwd");
        if (pwd) {
            document.getElementById("pwd").style.display = "block";
        }

    } else {

        document.getElementById("sed").style.display = "none";
        document.getElementById("eed").style.display = "none";
        document.getElementById("subAdmind").style.display = "none";

    }
}

function showSe() {

    var admin = document.getElementById("admin");
    var ce1 = document.getElementById("ce1");
    var ce2 = document.getElementById("ce2");
    var se = document.getElementById("se");
    var ee = document.getElementById("ee");

    admin.name = "";
    ce1.name = "";
    ce2.name = "";
    se.name = "username";
    ee.name = "";

    if (document.getElementById("sed").style.display === "none") {

        document.getElementById("ce1d").style.display = "block";
        document.getElementById("ce2d").style.display = "block";
        document.getElementById("sed").style.display = "block";
        var pwd = document.getElementById("pwd");
        if (pwd) {
            document.getElementById("pwd").style.display = "block";
        }

    } else {

        document.getElementById("eed").style.display = "none";
        document.getElementById("subAdmind").style.display = "none";

    }
}

function showEe() {

    var admin = document.getElementById("admin");
    var ce1 = document.getElementById("ce1");
    var ce2 = document.getElementById("ce2");
    var se = document.getElementById("se");
    var ee = document.getElementById("ee");

    admin.name = "";
    ce1.name = "";
    ce2.name = "";
    se.name = "";
    ee.name = "username";

    if (document.getElementById("eed").style.display === "none") {

        document.getElementById("ce1d").style.display = "block";
        document.getElementById("ce2d").style.display = "block";
        document.getElementById("sed").style.display = "block";
        document.getElementById("eed").style.display = "block";

        var pwd = document.getElementById("pwd");
        if (pwd) {
            document.getElementById("pwd").style.display = "block";
        }


        ee.name = "username";
    } else {
        document.getElementById("ce1d").style.display = "none";
        document.getElementById("ce2d").style.display = "none";
        document.getElementById("sed").style.display = "none";
        document.getElementById("eed").style.display = "none";
        document.getElementById("subAdmind").style.display = "none";
        ee.name = "";
    }
}

function processLogin(x) {

    switch (x) {

        case ("CHIEF WATER RESOURCE (ADMIN)") : {
            admin();
            break;
        }
        case ("HOD") : {
            hod();
            break;
        }

        case ("deputyAdmin") : {
            deputyAdmin();
            break;
        }

        case ("subAdmin") : {
            subAdmin();
            break;
        }

        case ("CE I") : {
            showCe1();
            break;
        }
        case ("CE II") : {
            showCe2();
            break;
        }
        case ("SE") : {
            showSe();
            break;
        }
        case ("EE") : {
            showEe();
            break;
        }
    }

}




// document.getElementById('admin-button').addEventListener('click', function() {
//     document.getElementById('admin-form').style.display = 'block';
//     document.getElementById('department-form').style.display = 'none';
// });
//
// document.getElementById('department-button').addEventListener('click', function() {
//     document.getElementById('department-form').style.display = 'block';
//     document.getElementById('admin-form').style.display = 'none';
// });