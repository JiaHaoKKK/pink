const path = require('path');
const HtmlWebpackPlugin = require('html-webpack-plugin');
const CleanWebpackPlugin = require('clean-webpack-plugin');
const ExtractTextWebpackPlugin = require("extract-text-webpack-plugin");

module.exports = {
  entry: './src/index.tsx',
  plugins: [
    new CleanWebpackPlugin(),
    new ExtractTextWebpackPlugin("styles.css"),
    new HtmlWebpackPlugin({
      title: "Pink",
      inject: false,
      template: require('html-webpack-template'),
      appMountId: 'app',
      mobile: true,
    }),
  ],
  output: {
    filename: 'main.js',
    path: path.resolve(__dirname, 'dist')
  },
  module: {
    rules: [{
      test: /\.css$/,
      use: ExtractTextWebpackPlugin.extract(['css-loader'])
    },{
      test: /\.js$/,
      use: ['babel-loader'],
      exclude: /node_modules/
    },{
      test: /\.tsx?$/,
      use: ['ts-loader'],
      exclude: /node_modules/
    }]
  },
  resolve: {
    extensions: ['.tsx', '.ts', '.js']
  },
  // externals: {
  //   'react': 'React',
  //   'react-dom': 'ReactDOM'
  // }
};