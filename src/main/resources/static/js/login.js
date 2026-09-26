const loginForm = document.getElementById("loginForm");
const message = document.getElementById("message");

const loginButton = document.getElementById("loginButton");
const buttonText = document.getElementById("buttonText");
const loader = document.getElementById("loader");

const passwordInput = document.getElementById("password");
const togglePassword = document.getElementById("togglePassword");


/* =========================
   SHOW / HIDE PASSWORD
========================= */

togglePassword.addEventListener("click", function () {

    if (passwordInput.type === "password") {
        passwordInput.type = "text";
        togglePassword.textContent = "🙈";
    } else {
        passwordInput.type = "password";
        togglePassword.textContent = "👁";
    }

});


/* =========================
   MESSAGE
========================= */

function showMessage(text, type) {

    message.textContent = text;
    message.className = "message " + type;

}


/* =========================
   LOGIN
========================= */

loginForm.addEventListener("submit", async function (event) {

    event.preventDefault();

    const username =
        document.getElementById("username").value.trim();

    const password =
        document.getElementById("password").value;


    if (!username || !password) {

        showMessage(
            "Please enter username and password.",
            "error"
        );

        return;
    }


    /* Loading */

    loginButton.disabled = true;
    buttonText.textContent = "Signing in...";
    loader.style.display = "inline-block";

    message.className = "message";


    try {

        const response = await fetch("/api/auth/login", {

            method: "POST",

            headers: {
                "Content-Type": "application/json"
            },

            credentials: "include",

            body: JSON.stringify({
                username: username,
                password: password
            })

        });


        const data = await response.json();


        if (response.ok) {

            showMessage(
                "Login successful! Opening dashboard...",
                "success"
            );


            /*
             * Small delay so the user can see
             * the success message.
             */

            setTimeout(function () {

                window.location.href = "index.html";

            }, 700);

        } else {

            showMessage(
                data.message || "Invalid username or password.",
                "error"
            );

        }

    } catch (error) {

        console.error(error);

        showMessage(
            "Unable to connect to the server. Please start Spring Boot.",
            "error"
        );

    } finally {

        loginButton.disabled = false;
        buttonText.textContent = "Sign In";
        loader.style.display = "none";

    }

});