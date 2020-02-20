from wwo_hist import retrieve_hist_data
import os

# os.chdir("weather")
frequency = 3
start_date = '1-JAN-2018'
end_date = '31-JAN-2018'
api_key = 'bf7271ffb6da47f79e6151255201602'
location_list = ['EWR', 'Queens', 'Bronx', 'Manhattan', 'Staten_Island', 'Brooklyn']
hist_weather_data = retrieve_hist_data(api_key,
                                location_list,
                                start_date,
                                end_date,
                                frequency,
                                location_label = False,
                                export_csv = True,
                                store_df = True)