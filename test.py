from pyDataverse.api import NativeApi
import mlcroissant

base_url = 'https://dataverse.harvard.edu'
dset_doi = 'doi:10.7910/DVN/TJCLKP'
api = NativeApi(base_url)
export = api.get_dataset_export(dset_doi, export_format='croissant')
croissant_url = export.url
ds = mlcroissant.Dataset(croissant_url)
print(ds)