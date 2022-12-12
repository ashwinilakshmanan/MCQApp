import React, { useState, useEffect } from 'react';
import { Link, useLocation, useNavigate } from 'react-router-dom';
import { Button, Table } from 'reactstrap';
import { Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { IOption } from 'app/shared/model/option.model';
import { getEntities } from './option.reducer';

export const Option = () => {
  const dispatch = useAppDispatch();

  const location = useLocation();
  const navigate = useNavigate();

  const optionList = useAppSelector(state => state.option.entities);
  const loading = useAppSelector(state => state.option.loading);

  useEffect(() => {
    dispatch(getEntities({}));
  }, []);

  const handleSyncList = () => {
    dispatch(getEntities({}));
  };

  return (
    <div>
      <h2 id="option-heading" data-cy="OptionHeading">
        <Translate contentKey="mcqApp.option.home.title">Options</Translate>
        <div className="d-flex justify-content-end">
          <Button className="me-2" color="info" onClick={handleSyncList} disabled={loading}>
            <FontAwesomeIcon icon="sync" spin={loading} />{' '}
            <Translate contentKey="mcqApp.option.home.refreshListLabel">Refresh List</Translate>
          </Button>
          <Link to="/option/new" className="btn btn-primary jh-create-entity" id="jh-create-entity" data-cy="entityCreateButton">
            <FontAwesomeIcon icon="plus" />
            &nbsp;
            <Translate contentKey="mcqApp.option.home.createLabel">Create new Option</Translate>
          </Link>
        </div>
      </h2>
      <div className="table-responsive">
        {optionList && optionList.length > 0 ? (
          <Table responsive>
            <thead>
              <tr>
                <th>
                  <Translate contentKey="mcqApp.option.id">ID</Translate>
                </th>
                <th>
                  <Translate contentKey="mcqApp.option.option">Option</Translate>
                </th>
                <th>
                  <Translate contentKey="mcqApp.option.isCorrect">Is Correct</Translate>
                </th>
                <th>
                  <Translate contentKey="mcqApp.option.question">Question</Translate>
                </th>
                <th />
              </tr>
            </thead>
            <tbody>
              {optionList.map((option, i) => (
                <tr key={`entity-${i}`} data-cy="entityTable">
                  <td>
                    <Button tag={Link} to={`/option/${option.id}`} color="link" size="sm">
                      {option.id}
                    </Button>
                  </td>
                  <td>{option.option}</td>
                  <td>{option.isCorrect ? 'true' : 'false'}</td>
                  <td>{option.question ? <Link to={`/question/${option.question.id}`}>{option.question.id}</Link> : ''}</td>
                  <td className="text-end">
                    <div className="btn-group flex-btn-group-container">
                      <Button tag={Link} to={`/option/${option.id}`} color="info" size="sm" data-cy="entityDetailsButton">
                        <FontAwesomeIcon icon="eye" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.view">View</Translate>
                        </span>
                      </Button>
                      <Button tag={Link} to={`/option/${option.id}/edit`} color="primary" size="sm" data-cy="entityEditButton">
                        <FontAwesomeIcon icon="pencil-alt" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.edit">Edit</Translate>
                        </span>
                      </Button>
                      <Button tag={Link} to={`/option/${option.id}/delete`} color="danger" size="sm" data-cy="entityDeleteButton">
                        <FontAwesomeIcon icon="trash" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.delete">Delete</Translate>
                        </span>
                      </Button>
                    </div>
                  </td>
                </tr>
              ))}
            </tbody>
          </Table>
        ) : (
          !loading && (
            <div className="alert alert-warning">
              <Translate contentKey="mcqApp.option.home.notFound">No Options found</Translate>
            </div>
          )
        )}
      </div>
    </div>
  );
};

export default Option;
