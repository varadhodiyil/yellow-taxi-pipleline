
from rest_framework.generics import GenericAPIView
from rest_framework.response import Response
from rest_framework.parsers import JSONParser, MultiPartParser


class HourlyWindow(GenericAPIView):

    parser_class = ((JSONParser, MultiPartParser))

    def get(self, request, *args, **kwargs):
        _dict = dict()
        return Response(_dict)
