using System;
using System.Collections.Generic;
using System.IO;
using MathNet.Numerics.LinearAlgebra;
using MathNet.Numerics.LinearAlgebra.Double;

namespace AI_Engine
{
    public class AIEngine
    {
        public static AI[] AIs;

        public static int Inputs;
        public static int Outputs;
        public static int[] hidden;
        public static int AI_NR;
        static int GENSTEP_NR;
        public static int MR;

        public static int generationStep;
        static int generation;
        static Evolver evolver;
        static DataManager DataManager = new DataManager();

        public int getGeneration()
        {
            return generation;
        }

        public int getGenerationStep()
        {
            return generationStep;
        }

        public int getAINumberPerScoreList(int Number)
        {
            return evolver.score_list[Number];
        }

        public float getAIScorePerScoreList(int Number)
        {
            return evolver.tempPoints[evolver.score_list[Number]];
        }

        public void saveAI(int AI)
        {
            DataManager.saveAI(AI);
        }

        public void setupEngine(Boolean menu, int InputNumber, int[] hiddenDimension, int OutputNumber, int AINumber, int Generationsteps,
                int MutationRate)
        {
            if (!menu)
            {

                Inputs = InputNumber;
                hidden = hiddenDimension;
                Outputs = OutputNumber;
                AI_NR = AINumber;
                GENSTEP_NR = Generationsteps;
                MR = MutationRate;
                evolver = new Evolver();
                AIs = new AI[AI_NR];
                for (int i = 0; i < AI_NR; i++)
                {
                    AIs[i] = new AI(Generationsteps, InputNumber, hidden, OutputNumber);
                }
            }
        }

        public void setInput(int Input, double Value, int AI)
        {
            AIs[AI].setInput(Input, Value);
        }

        public void runEngine(int AI)
        {
            AIs[AI].run();
        }

        public double getOutput(int Output, int AI)
        {
            return AIs[AI].getOutput(Output);
        }

        public void setScore(float Score, int AI)
        {
            AIs[AI].setScore(Score);
        }

        public void nextGenerationStep()
        {
            if (generationStep < GENSTEP_NR - 1)
            {
                generationStep++;
            }
            else
            {
                generationStep = 0;
                evolver.scoreLister();
                evolver.evolve();
                generation++;
            }
        }
    }

    public class AI
    {
        private Matrix<double> Inputs;
        private Matrix<double>[] baises;
        private Matrix<double>[] weights;
        private Matrix<double> Outputs;
        private float[] points;

        private double sigmoid(double x)
        {
            return 2 / (1 + Math.Exp(-2 * x)) - 1;
        }

        public AI(int Generationsteps, int Inputs, int[] hidden, int Outputs)
        {
            points = new float[Generationsteps];
            Random random = new Random();
            this.Inputs = DenseMatrix.Create(1, Inputs, random.NextDouble() * 2 - 1);
            baises = new Matrix<double>[hidden.Length + 1];
            weights = new Matrix<double>[hidden.Length + 1];
            int last_layer = Inputs;
            for (int i = 0; i < hidden.Length; i++)
            {
                baises[i] = DenseMatrix.Create(1, hidden[i], random.NextDouble() * 2 - 1);
                weights[i] = DenseMatrix.Create(last_layer, hidden[i], random.NextDouble() * 2 - 1);
                last_layer = hidden[i];

            }
            baises[hidden.Length] = DenseMatrix.Create(1, Outputs, random.NextDouble() * 2 - 1);
            weights[hidden.Length] = DenseMatrix.Create(last_layer, Outputs, random.NextDouble() * 2 - 1);
        }

        public AI(int Generationsteps, int Inputs, Matrix<double>[] baises, Matrix<double>[] weights)
        {
            points = new float[Generationsteps];
            this.Inputs = Matrix<double>.Build.Random(1, Inputs);
            this.baises = baises;
            this.weights = weights;
        }

        public void setInput(int Input, double Value)
        {
            Inputs.At(0, Input, Value);
        }

        public void run()
        {
            Matrix<double> last_layer = Inputs;
            for (int i = 0; i < weights.Length; i++)
            {
                last_layer = last_layer.Multiply(weights[i]);
                last_layer = last_layer.Add(baises[i]);
                for (int j = 0; j < last_layer.ColumnCount; j++)
                {
                    last_layer.At(0, j, sigmoid(last_layer[0, j]));
                }
            }
            Outputs = last_layer;
        }

        public double getOutput(int Output)
        {
            return Outputs[0, Output];
        }

        public void setScore(float Score)
        {
            points[AIEngine.generationStep] = Score;
        }

        public float getAvarageScore()
        {
            float temp = 0;
            for (int i = 0; i < points.Length; i++)
            {
                temp += points[i];
            }
            return temp / points.Length;
        }

        public Matrix<double>[] getWeights()
        {
            return weights;
        }

        public Matrix<double>[] getBaises()
        {
            return baises;
        }

        public void setWeights(Matrix<double>[] weights)
        {
            this.weights = weights;
        }

        public void setBaises(Matrix<double>[] baises)
        {
            this.baises = baises;
        }
    }

    public class Evolver
    {
        public float[] tempPoints;
        public int[] score_list;

        public Evolver()
        {
            tempPoints = new float[AIEngine.AI_NR];
            score_list = new int[AIEngine.AI_NR];
        }

        public void scoreLister()
        {
            float score = 0;
            float score_2 = 0;
            int score_array = 0;
            Boolean[] score_check = new Boolean[AIEngine.AI_NR];

            for (int i = 0; i < AIEngine.AI_NR; i++)
            {
                tempPoints[i] = AIEngine.AIs[i].getAvarageScore();
                score_check[i] = false;
            }
            score = 99999999999999999999999999999999999999f;

            for (int i = 0; i < AIEngine.AI_NR; i++)
            {
                for (int j = 0; j < AIEngine.AI_NR; j++)
                {
                    if (score_2 <= tempPoints[j] && tempPoints[j] <= score && score_check[j] == false)
                    {
                        score_2 = tempPoints[j];
                        score_array = j;
                    }
                }
                score_check[score_array] = true;
                score = tempPoints[score_array];
                score_2 = 0;
                score_list[i] = score_array;
            }
        }

        public void evolve()
        {
            Random random = new Random();
            int parent = 0;
            for (int i = 0; i < AIEngine.AI_NR / 2; i++)
            {
                Matrix<double>[] baises = new Matrix<double>[AIEngine.hidden.Length + 1];
                Matrix<double>[] weights = new Matrix<double>[AIEngine.hidden.Length + 1];
                int last_layer = AIEngine.Inputs;
                for (int j = 0; j < AIEngine.hidden.Length; j++)
                {
                    baises[j] = DenseMatrix.Create(1, AIEngine.hidden[j], 0);
                    weights[j] = DenseMatrix.Create(last_layer, AIEngine.hidden[j], 0);
                    for (int k = 0; k < AIEngine.hidden[j]; k++)
                    {
                        if (random.Next(100) <= 50)
                        {
                            baises[j].At(0, k, AIEngine.AIs[score_list[parent]].getBaises()[j][0, k]);
                        }
                        else
                        {
                            baises[j].At(0, k, AIEngine.AIs[score_list[parent + 1]].getBaises()[j][0, k]);
                        }
                        if (random.Next(100) <= AIEngine.MR)
                        {
                            baises[j].At(0, k, random.Next() * 2 - 1);
                        }
                        for (int l = 0; l < last_layer; l++)
                        {
                            if (random.Next(100) <= 50)
                            {
                                weights[j].At(l, k, AIEngine.AIs[score_list[parent]].getWeights()[j][l, k]);
                            }
                            else
                            {
                                weights[j].At(l, k, AIEngine.AIs[score_list[parent + 1]].getWeights()[j][l, k]);
                            }
                            if (random.Next(100) <= AIEngine.MR)
                            {
                                weights[j].At(l, k, random.NextDouble() * 2 - 1);
                            }
                        }
                    }
                    last_layer = AIEngine.hidden[j];
                }
                baises[AIEngine.hidden.Length] = DenseMatrix.Create(1, AIEngine.Outputs, 0);
                weights[AIEngine.hidden.Length] = DenseMatrix.Create(last_layer, AIEngine.Outputs, 0);
                for (int j = 0; j < AIEngine.Outputs; j++)
                {
                    if (random.Next(100) <= 50)
                    {
                        baises[AIEngine.hidden.Length].At(0, j, AIEngine.AIs[score_list[parent]].getBaises()[AIEngine.hidden.Length][0, j]);
                    }
                    else
                    {
                        baises[AIEngine.hidden.Length].At(0, j, AIEngine.AIs[score_list[parent + 1]].getBaises()[AIEngine.hidden.Length][0, j]);
                    }
                    if (random.Next(100) <= AIEngine.MR)
                    {
                        baises[AIEngine.hidden.Length].At(0, j, random.NextDouble() * 2 - 1);
                    }
                    for (int k = 0; k < last_layer; k++)
                    {
                        if (random.Next(100) <= 50)
                        {
                            weights[AIEngine.hidden.Length].At(k, j, AIEngine.AIs[score_list[parent]].getWeights()[AIEngine.hidden.Length][k, j]);
                        }
                        else
                        {
                            weights[AIEngine.hidden.Length].At(k, j, AIEngine.AIs[score_list[parent + 1]].getWeights()[AIEngine.hidden.Length][k, j]);
                        }
                        if (random.Next(100) <= AIEngine.MR)
                        {
                            weights[AIEngine.hidden.Length].At(k, j, random.NextDouble() * 2 - 1);
                        }
                    }
                }


                AIEngine.AIs[score_list[i + (AIEngine.AI_NR / 2)]].setBaises(baises);
                AIEngine.AIs[score_list[i + (AIEngine.AI_NR / 2)]].setWeights(weights);
                if (parent > AIEngine.AI_NR / 2)
                {
                    parent = 0;
                }
                else
                {
                    parent += 2;
                }
            }
        }
    }

    public class DataManager
    {
        public void saveAI(int AI)
        {
            List<String> content = new List<String>();
            String name = AI + ". AI (" + AIEngine.Inputs + ", " + AIEngine.Outputs + ", " + AIEngine.hidden + ").txt";

            content.Add(AIEngine.Inputs.ToString());
            content.Add(AIEngine.Outputs.ToString());
            content.Add(AIEngine.hidden.Length.ToString());
            for (int i = 0; i < AIEngine.hidden.Length; i++)
            {
                content.Add(AIEngine.hidden[i].ToString());
            }
            Matrix<double>[] weights = AIEngine.AIs[AI].getWeights();
            for (int i = 0; i < weights.Length; i++)
            {
                for (int j = 0; j < weights[i].ColumnCount; j++)
                {
                    for (int k = 0; k < weights[i].RowCount; k++)
                    {
                        content.Add(weights[i][k, j].ToString());
                    }
                }
            }
            Matrix<double>[] baises = AIEngine.AIs[AI].getBaises();
            for (int i = 0; i < baises.Length; i++)
            {
                for (int j = 0; j < baises[i].ColumnCount; j++)
                {

                    content.Add(baises[i][0, j].ToString());

                }
            }
            File.WriteAllText(name, content.ToString());
        }
    }
}
