const canvas = document.getElementById('drawingCanvas');
const ctx = canvas.getContext('2d');
let drawing = false;
let selectedFile = null;

clearCanvas();

canvas.addEventListener('mousedown', startDrawing);
canvas.addEventListener('mouseup', stopDrawing);
canvas.addEventListener('mousemove', draw);

function startDrawing(e) {
    drawing = true;
    ctx.beginPath();
    ctx.moveTo(e.clientX - canvas.offsetLeft, e.clientY - canvas.offsetTop);
    e.preventDefault();
}

function stopDrawing() {
    drawing = false;
}

function draw(e) {
    if (!drawing) return;
    ctx.lineTo(e.clientX - canvas.offsetLeft, e.clientY - canvas.offsetTop);
    ctx.strokeStyle = "black";
    ctx.lineWidth = 18;
    ctx.stroke();
    e.preventDefault();
}

function clearCanvas() {
    ctx.fillStyle = "white";
    ctx.fillRect(0, 0, canvas.width, canvas.height);
}

async function uploadCanvasImage() {
    const dataUrl = canvas.toDataURL('image/png');
    const blob = await (await fetch(dataUrl)).blob();

    const formData = new FormData();
    formData.append('image', blob, 'drawing.png');

    await sendImage(formData);
}

function handleFileSelect() {
    const fileInput = document.getElementById('imageInput');
    selectedFile = fileInput.files[0];

    if (selectedFile) {
        document.getElementById('detectImageButton').disabled = false; // Enable detect button
        document.getElementById('symbol').innerText = 'Image selected. Click "Detect Image" to proceed.';
    }
}

async function detectUploadedFile() {
    if (selectedFile) {
        const formData = new FormData();
        formData.append('image', selectedFile);

        await sendImage(formData);
    }
}

async function sendImage(formData) {
    try {
        const response = await fetch('http://127.0.0.1:8080/api/symbolRecognition', {
            method: 'POST',
            body: formData,
            headers: {
                'Accept': 'application/json'
            }
        });

        if (!response.ok) {
            throw new Error('Network response was not ok.');
        }

        const result = await response.text(); 
        console.log('API Response:', result); 

        const predictedSymbol = result || 'No prediction available'; 

        console.log('Predicted Symbol Text:', `Predicted Symbol: ${predictedSymbol}`);

        document.getElementById('symbol').innerText = `Predicted Symbol: ${predictedSymbol}`;

    } catch (error) {
        console.error('Error:', error);
        document.getElementById('symbol').innerText = 'Error occurred while processing the image.';
    }
}
