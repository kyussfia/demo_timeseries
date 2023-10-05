import React, { useState } from 'react';
import axios from 'axios';

const backendUrl = 'http://localhost:8080/api';

const FileUploadComponent: React.FC = () => {
    const [selectedFile, setSelectedFile] = useState<File | null>(null);

    const handleFileChange = (e: React.ChangeEvent<HTMLInputElement>) => {
        if (e.target.files && e.target.files.length > 0) {
            const file = e.target.files[0];
            if (file.name.endsWith('.json')) {
                setSelectedFile(file);
            } else {
                console.error('Only .json files are supported');
            }
        }
    };

    const handleUpload = () => {
        if (selectedFile) {
            const reader = new FileReader();

            reader.onload = async (event) => {
                if (event.target && event.target.result) {
                    try {
                        const jsonData = JSON.parse(event.target.result as string);

                        const response = await axios.put(`${backendUrl}/time-series`, jsonData, {
                            headers: {
                                'Content-Type': 'application/json',
                            },
                        });

                        if (response.status === 201 || response.status === 200) {
                            console.log('File uploaded successfully');
                        } else {
                            console.error('File upload failed');
                        }
                    } catch (error) {
                        console.error('Error uploading file:', error);
                    }
                }
            };

            reader.readAsText(selectedFile);
        } else {
            console.error('No file selected');
        }
    };

    return (
        <div>
            <input
                type="file"
                accept=".json"
                onChange={handleFileChange}
            />
            <button onClick={handleUpload}>Upload</button>
        </div>
    );
};

export default FileUploadComponent;
