# import numpy as np
# import cv2
# from PIL import Image
# from fastapi import FastAPI, File, UploadFile, HTTPException
# from pydantic import BaseModel
# import tensorflow as tf
# import io
# from typing import Tuple, Dict
#
#
# app = FastAPI()
#
# # Load the model and display its summary
# MODEL_PATH = 'model-v0.87.keras'
# model = tf.keras.models.load_model(MODEL_PATH)
# model.summary()
#
# # Map class indices to ASCII characters
# CLASS_TO_ASCII: Dict[int, int] = {
#     0: 48, 1: 49, 2: 50, 3: 51, 4: 52, 5: 53, 6: 54, 7: 55, 8: 56,
#     9: 57, 10: 65, 11: 66, 12: 67, 13: 68, 14: 69, 15: 70, 16: 71,
#     17: 72, 18: 73, 19: 74, 20: 75, 21: 76, 22: 77, 23: 78, 24: 79,
#     25: 80, 26: 81, 27: 82, 28: 83, 29: 84, 30: 85, 31: 86, 32: 87,
#     33: 88, 34: 89, 35: 90, 36: 97, 37: 98, 38: 99, 39: 100, 40: 101,
#     41: 102, 42: 103, 43: 104, 44: 110, 45: 113, 46: 114, 47: 116
# }
#
#
# class PredictionResponse(BaseModel):
#     character: str
#
#
# def preprocess_image(image: Image.Image, target_size: Tuple[int, int] = (28, 28)) -> np.ndarray:
#     if image.mode != 'RGB':
#         image = image.convert('RGB')
#
#     image_array = np.array(image)
#     inverted_image_array = cv2.bitwise_not(image_array)
#     grayscale_image = cv2.cvtColor(inverted_image_array, cv2.COLOR_RGB2GRAY)
#     resized_image = cv2.resize(grayscale_image, target_size)
#     processed_image = resized_image.reshape(1, *target_size, 1)
#
#     return processed_image
#
#
# @app.post("/predict", response_model=PredictionResponse)
# async def predict(file: UploadFile = File(...)) -> PredictionResponse:
#     try:
#         image = Image.open(io.BytesIO(await file.read()))
#         processed_image = preprocess_image(image)
#         prediction = model.predict(processed_image)
#         predicted_class = np.argmax(prediction)
#         predicted_symbol = CLASS_TO_ASCII.get(predicted_class)
#         if predicted_symbol is None:
#             raise HTTPException(status_code=500, detail="Prediction failed")
#         return PredictionResponse(character=chr(predicted_symbol))
#     except Exception as e:
#         raise HTTPException(status_code=500, detail=f"An error occurred: {e}")
#
#
# if __name__ == "__main__":
#     import uvicorn
#
#     uvicorn.run(app, host="127.0.0.1", port=8001)

import numpy as np
import cv2
from PIL import Image
from fastapi import FastAPI, File, UploadFile, HTTPException
from pydantic import BaseModel
import tensorflow as tf
import io
from typing import Tuple, Dict
from fastapi.middleware.cors import CORSMiddleware

app = FastAPI()

# Add CORS middleware to your app
app.add_middleware(
    CORSMiddleware,
    allow_origins=["http://localhost:8080"],
    allow_credentials=True,
    allow_methods=["*"],
    allow_headers=["*"],
)

# Load the model and display its summary
MODEL_PATH = 'model-v0.87.keras'
model = tf.keras.models.load_model(MODEL_PATH)
model.summary()

# Map class indices to ASCII characters
CLASS_TO_ASCII: Dict[int, int] = {
    0: 48, 1: 49, 2: 50, 3: 51, 4: 52, 5: 53, 6: 54, 7: 55, 8: 56,
    9: 57, 10: 65, 11: 66, 12: 67, 13: 68, 14: 69, 15: 70, 16: 71,
    17: 72, 18: 73, 19: 74, 20: 75, 21: 76, 22: 77, 23: 78, 24: 79,
    25: 80, 26: 81, 27: 82, 28: 83, 29: 84, 30: 85, 31: 86, 32: 87,
    33: 88, 34: 89, 35: 90, 36: 97, 37: 98, 38: 99, 39: 100, 40: 101,
    41: 102, 42: 103, 43: 104, 44: 110, 45: 113, 46: 114, 47: 116
}

class PredictionResponse(BaseModel):
    character: str

def preprocess_image(image: Image.Image, target_size: Tuple[int, int] = (28, 28)) -> np.ndarray:
    if image.mode != 'RGB':
        image = image.convert('RGB')

    image_array = np.array(image)
    inverted_image_array = cv2.bitwise_not(image_array)
    grayscale_image = cv2.cvtColor(inverted_image_array, cv2.COLOR_RGB2GRAY)
    resized_image = cv2.resize(grayscale_image, target_size)
    processed_image = resized_image.reshape(1, *target_size, 1)

    return processed_image

@app.post("/predict", response_model=PredictionResponse)
async def predict(file: UploadFile = File(...)) -> PredictionResponse:
    try:
        image = Image.open(io.BytesIO(await file.read()))
        processed_image = preprocess_image(image)
        prediction = model.predict(processed_image)
        predicted_class = np.argmax(prediction)
        predicted_symbol = CLASS_TO_ASCII.get(predicted_class)
        if predicted_symbol is None:
            raise HTTPException(status_code=500, detail="Prediction failed")
        return PredictionResponse(character=chr(predicted_symbol))
    except Exception as e:
        raise HTTPException(status_code=500, detail=f"An error occurred: {e}")

if __name__ == "__main__":  # Corrected here
    import uvicorn
    uvicorn.run(app, host="127.0.0.1", port=8001)