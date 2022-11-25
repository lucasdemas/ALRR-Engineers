import os
from flask import Flask
from flask import render_template

app = Flask(__name__)

# ROUTES

@app.route('/')
def home_page():
    auth_token = open("auth_token.txt")
    auth_key = auth_token.read()
    print(auth_key)
    auth_token.close()
    return render_template('home_page.html', auth_key = auth_key)

if __name__ == '__main__':
   app.run(debug = True)




