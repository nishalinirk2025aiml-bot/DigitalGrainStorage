const registerForm = document.getElementById("registerForm");

const message = document.getElementById("message");

const passwordInput =
    document.getElementById("password");

const confirmPasswordInput =
    document.getElementById("confirmPassword");

const togglePassword =
    document.getElementById("togglePassword");

const strengthBar =
    document.getElementById("strengthBar");

const strengthText =
    document.getElementById("strengthText");

const registerButton =
    document.getElementById("registerButton");

const buttonText =
    document.getElementById("buttonText");

const loader =
    document.getElementById("loader");


/* =========================
   PASSWORD VISIBILITY
========================= */

togglePassword.addEventListener("click", function () {

    if (passwordInput.type === "password") {

        passwordInput.type = "text";

        confirmPasswordInput.type = "text";

        togglePassword.textContent = "🙈";

    } else {

        passwordInput.type = "password";

        confirmPasswordInput.type = "password";

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
   PASSWORD STRENGTH
========================= */

passwordInput.addEventListener("input", function () {

    const password = passwordInput.value;

    let strength = 0;


    if (password.length >= 6) {
        strength++;
    }

    if (password.length >= 8) {
        strength++;
    }

    if (/[A-Z]/.test(password)) {
        strength++;
    }

    if (/[0-9]/.test(password)) {
        strength++;
    }

    if (/[^A-Za-z0-9]/.test(password)) {
        strength++;
    }


    const percentage = (strength / 5) * 100;

    strengthBar.style.width = percentage + "%";


    if (password.length === 0) {

        strengthText.textContent =
            "Password strength";

    } else if (strength <= 2) {

        strengthText.textContent =
            "Weak password";

        strengthBar.style.background =
            "#e74c3c";

    } else if (strength <= 3) {

        strengthText.textContent =
            "Medium password";

        strengthBar.style.background =
            "#f39c12";

    } else {

        strengthText.textContent =
            "Strong password";

        strengthBar.style.background =
            "#45a35a";
    }

});


/* =========================
   REGISTER
========================= */

registerForm.addEventListener("submit", async function (event) {

    event.preventDefault();


    const username =
        document.getElementById("username")
            .value.trim();

    const password =
        passwordInput.value;

    const confirmPassword =
        confirmPasswordInput.value;

    const terms =
        document.getElementById("terms").checked;


    /* VALIDATION */

    if (!username) {

        showMessage(
            "Please enter a username.",
            "error"
        );

        return;
    }


    if (password.length < 6) {

        showMessage(
            "Password must contain at least 6 characters.",
            "error"
        );

        return;
    }


    if (password !== confirmPassword) {

        showMessage(
            "Passwords do not match.",
            "error"
        );

        return;
    }


    if (!terms) {

        showMessage(
            "Please accept the terms before creating an account.",
            "error"
        );

        return;
    }


    /* LOADING */

    registerButton.disabled = true;

    buttonText.textContent =
        "Creating account...";

    loader.style.display =
        "inline-block";

    message.className =
        "message";


    try {

        const response = await fetch(
            "/api/auth/register",
            {
                method: "POST",

                headers: {
                    "Content-Type": "application/json"
                },

                body: JSON.stringify({
                    username: username,
                    password: password
                })
            }
        );


        const data =
            await response.json();


        if (response.ok) {

            showMessage(
                "Account created successfully! Redirecting to login...",
                "success"
            );


            setTimeout(function () {

                window.location.href =
                    "login.html";

            }, 1200);


        } else {

            showMessage(
                data.message ||
                "Unable to create account.",
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

        registerButton.disabled = false;

        buttonText.textContent =
            "Create Account";

        loader.style.display =
            "none";

    }

});