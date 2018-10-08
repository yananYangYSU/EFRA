from __future__ import print_function, division
from keras.layers import Input, Dense, Dropout
from keras.layers import BatchNormalization, Activation, Concatenate,Reshape,LSTM
from keras.models import Model
from keras.optimizers import Adam
import matplotlib.pyplot as plt
import numpy as np
from pandas import read_csv
from sklearn.preprocessing import MinMaxScaler

class CGAN():
    def __init__(self):
        # number of hidden units in LSTM cells
        self.hidden_len = 24
        # length of LSTM input
        self.condition_len = 48
        # bulid LSTM model
        self.prediction = self.bulid_lstm()
        # create optimier and compile the LSTM model
        adam = Adam(lr=0.0009, beta_1=0.9, beta_2=0.999, epsilon=1e-08)
        self.prediction.compile(loss='mse', optimizer=adam, metrics=['accuracy'])



    def bulid_lstm(self):

        ip = Input(shape=(48,))
        y = Reshape([48, 1])(ip)
        y = LSTM(self.hidden_len, dropout=0.2, recurrent_dropout=0.2)(y)
        y = Dense(24)(y)
        output = Activation('relu')(y)

        model = Model(ip, output)
        model.summary()
        return model


    def train(self, epochs, save_interval=50):

        dataframe = read_csv('data.csv')
        dataset = dataframe.values
        dataset = dataset.astype('float32')
        dataset = np.array(dataset)
        scaler = MinMaxScaler(feature_range=(0, 1))
        dataset = scaler.fit_transform(dataset)

        #distribute raw data
        x = np.zeros((len(dataset)-72, 72))
        for i in range(len(dataset)-72):
            x[i] = np.reshape(dataset[i:i+72],72)



        # training set and testing set
        x = x [:len(x)-1]
        y = x[-1]


        for epoch in range(epochs):
            #Train model
            conditions = x[:, 0:self.condition_len]
            result = x[:, self.condition_len:]
            loss = self.prediction.train_on_batch(conditions, result)

            # Testing modelmt
            if epoch % save_interval == 0:
                test_con = y[0:self.condition_len]
                test_con = np.reshape(test_con,(1,48))
                test_res = y[self.condition_len:]
                res = self.prediction.predict(test_con)
                res = np.reshape(res,(1,24))
                plt.title('Prediction by LSTM \n Training time: %d'%(epoch))
                plt.plot(test_res)
                plt.plot(res[0])
                plt.legend(['Data',  'Prediction'])
                plt.show()


if __name__ == '__main__':
    cgan = CGAN()
    cgan.train(epochs=1000,  save_interval=100)
