package com.example.quakereport;

import java.util.ArrayList;

public class QuickSort {
    /* This function takes last element as pivot,
       places the pivot element at its correct
       position in sorted array, and places all
       smaller (smaller than pivot) to left of
       pivot and all greater elements to right
       of pivot */
//    public int partition(ArrayList<Country> countryData, int low, int high) {
//        String pivot = countryData.get(high).getCountryName();
////        int pivot = arr[high];
//        int i = (low - 1); // index of smaller element
//        for (int j = low; j < high; j++) {
//            // If current element is smaller than the pivot
////            if (arr[j] < pivot) {
////                i++;
////
////                // swap arr[i] and arr[j]
////                int temp = arr[i];
////                arr[i] = arr[j];
////                arr[j] = temp;
////            }
//
//            if(countryData.get(j).getCountryName().compareTo(pivot) == -1){
//                i++;
//
//                Country temp = countryData.get(i);
//                countryData.set(i, countryData.get(j));
//                countryData.set(j, temp);
//            }
//        }
//
//        // swap arr[i+1] and arr[high] (or pivot)
////        int temp = arr[i + 1];
////        arr[i + 1] = arr[high];
////        arr[high] = temp;
//
//        Country temp = countryData.get(i + 1);
//        countryData.set(i+1, countryData.get(high));
//        countryData.set(high, temp);
//        return i + 1;
//    }

    public int partitionMagnitudeDescending(ArrayList<EarthquakeData> earthquakeData, int low, int high) {
        Double pivot = earthquakeData.get(high).getMagnitude();
//        int pivot = arr[high];
        int i = (low - 1); // index of smaller element
        for (int j = low; j < high; j++) {
            // If current element is smaller than the pivot
//            if (arr[j] < pivot) {
//                i++;
//
//                // swap arr[i] and arr[j]
//                int temp = arr[i];
//                arr[i] = arr[j];
//                arr[j] = temp;
//            }
            if(earthquakeData.get(j).getMagnitude() > pivot){
                i++;

                EarthquakeData temp = earthquakeData.get(i);
                earthquakeData.set(i, earthquakeData.get(j));
                earthquakeData.set(j, temp);
            }
        }

        // swap arr[i+1] and arr[high] (or pivot)
//        int temp = arr[i + 1];
//        arr[i + 1] = arr[high];
//        arr[high] = temp;

        EarthquakeData temp = earthquakeData.get(i + 1);
        earthquakeData.set(i+1, earthquakeData.get(high));
        earthquakeData.set(high, temp);
        return i + 1;
    }
    public int partitionMagnitudeAscending(ArrayList<EarthquakeData> earthquakeData, int low, int high) {
        Double pivot = earthquakeData.get(high).getMagnitude();
//        int pivot = arr[high];
        int i = (low - 1); // index of smaller element
        for (int j = low; j < high; j++) {
            // If current element is smaller than the pivot
//            if (arr[j] < pivot) {
//                i++;
//
//                // swap arr[i] and arr[j]
//                int temp = arr[i];
//                arr[i] = arr[j];
//                arr[j] = temp;
//            }
            if(earthquakeData.get(j).getMagnitude() < pivot){
                i++;

                EarthquakeData temp = earthquakeData.get(i);
                earthquakeData.set(i, earthquakeData.get(j));
                earthquakeData.set(j, temp);
            }
        }

        // swap arr[i+1] and arr[high] (or pivot)
//        int temp = arr[i + 1];
//        arr[i + 1] = arr[high];
//        arr[high] = temp;

        EarthquakeData temp = earthquakeData.get(i + 1);
        earthquakeData.set(i+1, earthquakeData.get(high));
        earthquakeData.set(high, temp);
        return i + 1;
    }



    /* The main function that implements QuickSort()
      arr[] --> Array to be sorted,
      low  --> Starting index,
      high  --> Ending index */
    public ArrayList<EarthquakeData> sort(ArrayList<EarthquakeData> earthquakeData , int low, int high, boolean ascending) {
        if (low < high) {
            /* pi is partitioning index, arr[pi] is
              now at right place */
            int pi = 0;
            if (ascending){
                pi = partitionMagnitudeAscending(earthquakeData, low, high);
            }
            else{
                pi = partitionMagnitudeDescending(earthquakeData, low, high);
            }

            // Recursively sort elements before
            // partition and after partition
            sort(earthquakeData, low, pi - 1, ascending);
            sort(earthquakeData, pi + 1, high,ascending);
        }
        return earthquakeData;
    }

}