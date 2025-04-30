const express = require('express');
const cors = require('cors');
const app = express();
const PORT = 5002;

app.use(cors());
app.use(express.json());

const dummyQuestions = {
  "AI": [
    { questionText: "What does AI stand for?", options: ["Artificial Intelligence", "Analog Interface", "Actual Instance", "None"], correctAnswer: 0 },
    { questionText: "Which of these is an AI field?", options: ["Computer Vision", "Mechanical Engineering", "Civil Engineering", "Architecture"], correctAnswer: 0 },
    { questionText: "Which is an example of AI?", options: ["Self-driving cars", "Cooking food", "Brushing teeth", "None"], correctAnswer: 0 },
    { questionText: "Which programming language is widely used for AI?", options: ["Python", "Java", "C", "PHP"], correctAnswer: 0 },
    { questionText: "Deep Learning is a subfield of?", options: ["Machine Learning", "Software Engineering", "Data Entry", "None"], correctAnswer: 0 }
  ],
  "Data Structures": [
    { questionText: "Which data structure uses FIFO?", options: ["Queue", "Stack", "Tree", "Graph"], correctAnswer: 0 },
    { questionText: "Which data structure uses LIFO?", options: ["Stack", "Queue", "Array", "Heap"], correctAnswer: 0 },
    { questionText: "Which of these is a linear data structure?", options: ["Linked List", "Tree", "Graph", "Hash Table"], correctAnswer: 0 },
    { questionText: "Which structure is best for hierarchy?", options: ["Tree", "Stack", "Queue", "Array"], correctAnswer: 0 },
    { questionText: "Which structure uses nodes and edges?", options: ["Graph", "Queue", "Stack", "Array"], correctAnswer: 0 }
  ],
  "Web Development": [
    { questionText: "What does HTML stand for?", options: ["Hyper Text Markup Language", "High Text Markup Language", "Hyperlinks Text Mark Language", "None"], correctAnswer: 0 },
    { questionText: "Which is used for styling webpages?", options: ["CSS", "HTML", "JavaScript", "PHP"], correctAnswer: 0 },
    { questionText: "Which language is used to make web pages interactive?", options: ["JavaScript", "CSS", "HTML", "Python"], correctAnswer: 0 },
    { questionText: "Which tag is used for hyperlinks?", options: ["<a>", "<link>", "<p>", "<h1>"], correctAnswer: 0 },
    { questionText: "Which framework is popular for frontend?", options: ["ReactJS", "Node.js", "Flask", "Django"], correctAnswer: 0 }
  ],
  "Testing": [
    { questionText: "What does QA stand for?", options: ["Quality Assurance", "Quick Access", "Quality Accuracy", "Query Analysis"], correctAnswer: 0 },
    { questionText: "Which testing is done without program execution?", options: ["Static Testing", "Dynamic Testing", "Performance Testing", "None"], correctAnswer: 0 },
    { questionText: "JUnit is used for?", options: ["Unit Testing", "System Testing", "Acceptance Testing", "Integration Testing"], correctAnswer: 0 },
    { questionText: "Which is NOT a type of software testing?", options: ["Black-box Testing", "White-box Testing", "Grey-box Testing", "Blue-box Testing"], correctAnswer: 3 },
    { questionText: "Selenium is used for?", options: ["Automation Testing", "Manual Testing", "Performance Testing", "Security Testing"], correctAnswer: 0 }
  ],
  "Algorithms": [
    { questionText: "Which sorting algorithm is fastest in general cases?", options: ["Quick Sort", "Bubble Sort", "Insertion Sort", "Selection Sort"], correctAnswer: 0 },
    { questionText: "Binary Search works on?", options: ["Sorted array", "Unsorted array", "Both", "None"], correctAnswer: 0 },
    { questionText: "Which algorithm is used in Graphs?", options: ["Dijkstra's Algorithm", "Binary Search", "Bubble Sort", "Stack Operations"], correctAnswer: 0 },
    { questionText: "Which search algorithm explores neighbors first?", options: ["BFS", "DFS", "Binary Search", "Ternary Search"], correctAnswer: 0 },
    { questionText: "Which of these is not a sorting algorithm?", options: ["DFS", "Merge Sort", "Quick Sort", "Bubble Sort"], correctAnswer: 0 }
  ],
  "Cloud Computing": [
    { questionText: "What is SaaS?", options: ["Software as a Service", "Storage as a Service", "Solution as a Software", "None"], correctAnswer: 0 },
    { questionText: "Which is a Cloud provider?", options: ["AWS", "Azure", "GCP", "All of the above"], correctAnswer: 3 },
    { questionText: "IaaS means?", options: ["Infrastructure as a Service", "Internet as a Service", "Instance as a Service", "None"], correctAnswer: 0 },
    { questionText: "Cloud Computing provides?", options: ["Scalability", "Flexibility", "Cost Saving", "All of these"], correctAnswer: 3 },
    { questionText: "Which is a deployment model?", options: ["Public Cloud", "Private Cloud", "Hybrid Cloud", "All of the above"], correctAnswer: 3 }
  ],
  "Mobile Apps": [
    { questionText: "Android is based on which kernel?", options: ["Linux", "Windows", "iOS", "Mac"], correctAnswer: 0 },
    { questionText: "Which language is used for Android development?", options: ["Java", "Swift", "Python", "C#"], correctAnswer: 0 },
    { questionText: "What is the entry point of Android app?", options: ["MainActivity", "SplashScreen", "Settings", "Menu"], correctAnswer: 0 },
    { questionText: "Intent is used for?", options: ["Navigation between screens", "Database", "Network communication", "UI Design"], correctAnswer: 0 },
    { questionText: "Which file contains app metadata?", options: ["AndroidManifest.xml", "build.gradle", "MainActivity.java", "Strings.xml"], correctAnswer: 0 }
  ],
  "Machine Learning": [
    { questionText: "ML stands for?", options: ["Machine Learning", "Manual Learning", "Molecular Learning", "None"], correctAnswer: 0 },
    { questionText: "In supervised learning, data is?", options: ["Labeled", "Unlabeled", "Random", "Encrypted"], correctAnswer: 0 },
    { questionText: "K-Means is which type of algorithm?", options: ["Clustering", "Classification", "Regression", "Association"], correctAnswer: 0 },
    { questionText: "Which is NOT a ML algorithm?", options: ["Linear Regression", "Decision Tree", "Bubble Sort", "Random Forest"], correctAnswer: 2 },
    { questionText: "Overfitting happens when?", options: ["Model fits training data too well", "Model underfits data", "Model ignores data", "None"], correctAnswer: 0 }
  ],
  "General": [
    { questionText: "Earth is a?", options: ["Planet", "Star", "Galaxy", "None"], correctAnswer: 0 },
    { questionText: "Water freezes at?", options: ["0°C", "100°C", "50°C", "None"], correctAnswer: 0 },
    { questionText: "Which one is a programming language?", options: ["Python", "Snake", "Lizard", "None"], correctAnswer: 0 },
    { questionText: "Which planet is closest to sun?", options: ["Mercury", "Venus", "Mars", "Earth"], correctAnswer: 0 },
    { questionText: "How many continents?", options: ["5", "6", "7", "8"], correctAnswer: 2 }
  ]
};

app.get('/getQuestions', (req, res) => {
  const topic = req.query.topic;
  const questions = dummyQuestions[topic] || dummyQuestions["General"];
  res.json(questions);
});

app.listen(PORT, () => {
  console.log(`Server running on http://localhost:${PORT}`);
});
