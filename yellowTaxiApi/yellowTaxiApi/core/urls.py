from django.conf.urls import url , include
from yellowTaxiApi.core import views

urlpatterns = [
    url(r'^hourly/$', views.HourlyWindow.as_view()),
    # url(r'^prosthetics/$',views.ProstheticsAPI.as_view())
]