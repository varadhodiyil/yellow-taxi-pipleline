from django.conf.urls import url , include
from yellowTaxiApi.core import views

urlpatterns = [
    url(r'^hourly/$', views.HourlyWindow.as_view()),
    url(r'^daily/$', views.DailyWindow.as_view())
]