import json
import os
from flask import Flask
from flask import render_template
from flask import Response, request, jsonify

app = Flask(__name__)

# ROUTES

@app.route('/')
def home_page():
    api_token = open("api_token.txt")
    api_key = api_token.read()
    print(api_key)
    api_token.close()
    return render_template('home_page.html', api_key = api_key)

if __name__ == '__main__':
   app.run(debug = True)




